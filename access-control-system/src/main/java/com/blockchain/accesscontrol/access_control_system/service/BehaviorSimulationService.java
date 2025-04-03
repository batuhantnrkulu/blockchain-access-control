package com.blockchain.accesscontrol.access_control_system.service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.RoleToken;
import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.BehaviorHistoryRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

@Service
public class BehaviorSimulationService extends BaseContractService<RoleToken> {

	private static final int BEHAVIORS_PER_PEER = 10;
	private static final double PENALTY_PROBABILITY = 0.4;
	private static final double VALIDATOR_PROBABILITY = 0.2;
	private static final int SIMULATION_DAYS = 30;

	private final PeerRepository peerRepository;
	private final BehaviorHistoryRepository behaviorHistoryRepo;

	private final Map<String, PenaltyConfig> penalties = createPenalties();

	@Autowired
	public BehaviorSimulationService(Web3j web3j, TransactionManagerFactory transactionManagerFactory,
			@Value("${contract.roleTokenAddress}") String contractAddress, PeerRepository peerRepository,
			BehaviorHistoryRepository behaviorHistoryRepo) {
		super(web3j, transactionManagerFactory, contractAddress);
		this.peerRepository = peerRepository;
		this.behaviorHistoryRepo = behaviorHistoryRepo;
	}

	private Map<String, PenaltyConfig> createPenalties() {
		Map<String, PenaltyConfig> penalties = new LinkedHashMap<>();
		penalties.put("Unauthorized access attempt",
				new PenaltyConfig("major", List.of(9000L, 7000L, 5000L), Duration.ofHours(1)));
		penalties.put("Too frequent access",
				new PenaltyConfig("minor", List.of(5000L, 3000L, 1000L), Duration.ofMinutes(30)));
		penalties.put("Data leakage attempt",
				new PenaltyConfig("major", List.of(11000L, 9000L, 7000L), Duration.ofHours(2)));
		penalties.put("Privilege escalation",
				new PenaltyConfig("major", List.of(14000L, 12000L, 10000L), Duration.ofHours(5)));
		penalties.put("Spoofing attempt",
				new PenaltyConfig("minor", List.of(6000L, 4000L, 2000L), Duration.ofMinutes(20)));
		penalties.put("Repudiation attempt",
				new PenaltyConfig("minor", List.of(7000L, 5000L, 3000L), Duration.ofHours(1)));
		penalties.put("Tampering with data",
				new PenaltyConfig("major", List.of(12000L, 10000L, 8000L), Duration.ofHours(3)));
		penalties.put("Denial of Service",
				new PenaltyConfig("major", List.of(24000L, 22000L, 20000L), Duration.ofDays(2)));
		return Collections.unmodifiableMap(penalties);
	}

	private static class BehaviorTemplate {
		private final String reason;
		private final long amountDelta;
		private final boolean positive;
		private final Status newStatus;
		private final LocalDateTime timestamp;

		public BehaviorTemplate(String reason, long amountDelta, boolean positive, Status newStatus,
				LocalDateTime timestamp) {
			this.reason = reason;
			this.amountDelta = amountDelta;
			this.positive = positive;
			this.newStatus = newStatus;
			this.timestamp = timestamp;
		}

		public String getReason() {
			return reason;
		}

		public long getAmountDelta() {
			return amountDelta;
		}

		public boolean isPositive() {
			return positive;
		}

		public Status getNewStatus() {
			return newStatus;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}
	}

	public void simulatePeerBehaviors(Peer peer) {
		List<BehaviorTemplate> templates = generateBehaviorTemplates(peer);
		templates.forEach(template -> processBehaviorTemplate(peer, template));
	}

	// Update template generation to pass previous templates
	private List<BehaviorTemplate> generateBehaviorTemplates(Peer peer) {
		List<BehaviorTemplate> templates = new ArrayList<>();
		Random random = new Random();
		LocalDateTime startTime = LocalDateTime.now().minusDays(SIMULATION_DAYS);

		for (int i = 0; i < BEHAVIORS_PER_PEER; i++) {
			LocalDateTime timestamp = startTime.plusDays(i * 3L).plusHours(random.nextInt(24));
			templates.add(createBehaviorTemplate(peer, timestamp, random, templates));
		}

		return templates;
	}

	private BehaviorTemplate createBehaviorTemplate(Peer peer, LocalDateTime timestamp, Random random,
			List<BehaviorTemplate> previousTemplates) {
		if (peer.getRole() == Role.PRIMARY_GROUP_HEAD && random.nextDouble() < VALIDATOR_PROBABILITY) {
			return createValidatorTemplate(peer, timestamp, random);
		}
		return random.nextDouble() < PENALTY_PROBABILITY ? createPenaltyTemplate(peer, timestamp, random)
				: createRecoveryTemplate(peer, timestamp, previousTemplates);
	}

	private BehaviorTemplate createPenaltyTemplate(Peer peer, LocalDateTime timestamp, Random random) {
		String reason = new ArrayList<>(penalties.keySet()).get(random.nextInt(penalties.size()));
		PenaltyConfig config = penalties.get(reason);
		long deduction = getDeduction(peer.getRole(), config.deductions());

		return new BehaviorTemplate(reason, -deduction, false,
				config.severity().equals("major") ? Status.MALICIOUS : Status.SUSPICIOUS, timestamp);
	}

	private Long getDeduction(Role role, List<Long> deductions) {
		return switch (role) {
		case PRIMARY_GROUP_HEAD -> deductions.get(0);
		case SECONDARY_GROUP_HEAD -> deductions.get(1);
		default -> deductions.get(2);
		};
	}

	private BehaviorTemplate createRecoveryTemplate(Peer peer, LocalDateTime timestamp,
			List<BehaviorTemplate> previousTemplates) 
	{
		// Track simulated state changes
		Status simulatedStatus = peer.getBcStatus();
		LocalDateTime lastPenaltyTime = null;

		// Find last penalty in simulated history
		for (BehaviorTemplate t : previousTemplates) {
			if (!t.isPositive()) {
				lastPenaltyTime = t.getTimestamp();
			}
		}
		
		if (previousTemplates.size() > 0)
			simulatedStatus = previousTemplates.get(previousTemplates.size() - 1) .getNewStatus();

		// Calculate recovery based on simulated timeline
		Status calculatedStatus = simulatedStatus;
		boolean isSustained = false;
		
		if (lastPenaltyTime != null) {
			Duration duration = Duration.between(lastPenaltyTime, timestamp);
			calculatedStatus = switch (simulatedStatus) {
			case SUSPICIOUS -> duration.toDays() >= 1 ? Status.BENIGN : simulatedStatus;
			case MALICIOUS -> duration.toDays() >= 2 ? Status.BENIGN : simulatedStatus;
			default -> simulatedStatus;
			};
			
			isSustained = calculatedStatus == Status.BENIGN && simulatedStatus == Status.BENIGN && duration.toDays() >= 1;
		}

		// Determine reward and reason
		boolean isRecovery = calculatedStatus != simulatedStatus;

		return new BehaviorTemplate(
				isRecovery ? "Status Recovered" : isSustained ? "Sustained Good Behavior" : "",
				isSustained ? 5000 : 0, true, calculatedStatus, timestamp);
	}

	private BehaviorTemplate createValidatorTemplate(Peer peer, LocalDateTime timestamp, Random random) {
		boolean responded = random.nextDouble() < 0.7;
		return new BehaviorTemplate(responded ? "Validator Response Success" : "Validator Response Failure",
				responded ? 1000 : -1000, responded, responded ? peer.getBcStatus() : Status.SUSPICIOUS, timestamp);
	}

	private void processBehaviorTemplate(Peer peer, BehaviorTemplate template) {
		try {
			
			Optional<Peer> peerOpt = peerRepository.findByBcAddress(peer.getBcAddress());
			
			if (peerOpt.isPresent()) {
				peer = peerOpt.get();			
			}
			
			// 1. Apply blockchain operation first
			Long newBalance = template.getAmountDelta() >= 0
					? rewardMemberAndUpdate(peer.getBcAddress(), template.getAmountDelta())
					: penalizeMemberAndUpdate(peer.getBcAddress(), -template.getAmountDelta());

			// 2. Update status
			if (template.getNewStatus() != peer.getBcStatus()) {
				
				loadContract(RoleToken.class)
						.updateMemberStatus(peer.getBcAddress(), template.getNewStatus().toString()).send();

				peer.setBcStatus(template.getNewStatus());
				peerRepository.saveAndFlush(peer);
			}

			if (!template.getReason().isBlank()) {
				// 3. Create and save behavior history with updated values
				BehaviorHistory history = new BehaviorHistory();
				history.setPeer(peer);
				history.setTokenAmount(newBalance);
				history.setTokenAmountChange(template.getAmountDelta());
				history.setReason(template.getReason());
				history.setStatusUpdate(template.getTimestamp());
				history.setPositive(template.isPositive());
				behaviorHistoryRepo.save(history);
			}

		} catch (Exception e) {
			throw new RuntimeException("Behavior processing failed", e);
		}
	}

	/**
	 * Deducts the specified penalty amount from the given member both on-chain via
	 * the contract, and off-chain by updating the peer record in the database. Also
	 * sends a notification alerting the member of the penalty.
	 *
	 * @param memberAddress The blockchain address of the member.
	 * @param penaltyAmount The penalty amount to deduct.
	 */
	public Long penalizeMemberAndUpdate(String memberAddress, long penaltyAmount) {
		RoleToken contract = loadContract(RoleToken.class);

		try {
			BigInteger penalty = BigInteger.valueOf(penaltyAmount);

			// Call contract function to penalize the member.
			TransactionReceipt receipt = contract.penalizeMember(memberAddress, penalty).send();
			System.out.println("Penalty Transaction Receipt: " + receipt.getTransactionHash());

			// Update the member's balance in the local database.
			Optional<Peer> peerOpt = peerRepository.findByBcAddress(memberAddress);

			if (peerOpt.isPresent()) {
				Peer peer = peerOpt.get();
				long currentBalance = peer.getErc20TokenAmount();
				long newBalance = currentBalance - penaltyAmount;
				peer.setErc20TokenAmount(newBalance);
				peer.setMisbehaviorCounter(peer.getMisbehaviorCounter() + 1);
				peerRepository.saveAndFlush(peer);

				return newBalance;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Rewards the member with the specified amount on-chain and off-chain.
	 *
	 * @param memberAddress The blockchain address of the member.
	 * @param rewardAmount  The reward amount to add.
	 */
	public Long rewardMemberAndUpdate(String memberAddress, long rewardAmount) {
		RoleToken contract = loadContract(RoleToken.class);

		try {
			BigInteger reward = BigInteger.valueOf(rewardAmount);

			// Call contract function to add tokens to the member.
			TransactionReceipt receipt = contract.rewardMember(memberAddress, reward).send();
			System.out.println("Reward Transaction Receipt: " + receipt.getTransactionHash());

			// Update the member's token balance in the database.
			Optional<Peer> peerOpt = peerRepository.findByBcAddress(memberAddress);

			if (peerOpt.isPresent()) {
				Peer peer = peerOpt.get();

				long currentBalance = peer.getErc20TokenAmount();
				long newBalance = currentBalance + rewardAmount;
				peer.setErc20TokenAmount(newBalance);
				peer.setRewardCounter(peer.getRewardCounter() + 1);
				peerRepository.saveAndFlush(peer);

				return newBalance;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static class PenaltyConfig {
		private final String severity;
		private final List<Long> deductions;
		private final Duration blockDuration;

		PenaltyConfig(String severity, List<Long> deductions, Duration blockDuration) {
			this.severity = severity;
			this.deductions = deductions;
			this.blockDuration = blockDuration;
		}

		// Add getters
		public String severity() {
			return severity;
		}

		public List<Long> deductions() {
			return deductions;
		}

		public Duration blockDuration() {
			return blockDuration;
		}
	}
}
