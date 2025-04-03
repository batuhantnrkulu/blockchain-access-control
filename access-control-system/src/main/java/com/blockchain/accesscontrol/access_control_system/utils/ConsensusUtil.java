package com.blockchain.accesscontrol.access_control_system.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.blockchain.accesscontrol.access_control_system.model.BehaviorHistory;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.BehaviorHistoryRepository;

@Component
public class ConsensusUtil 
{
    // Weight configuration
    private static final double TOKEN_WEIGHT = 0.40;
    private static final double HISTORIC_BEHAVIOR_WEIGHT = 0.35;
    private static final double RECENT_BEHAVIOR_WEIGHT = 0.25;
    private static final int RECENT_DAYS = 7;

    public List<Peer> selectWeightedRandom(List<Peer> peers, int count, BehaviorHistoryRepository behaviorRepo) 
    {
        List<Peer> selected = new ArrayList<>();
        
        if (peers.isEmpty()) 
        	return selected;

        // Pre-calculate values
        long maxToken = peers.stream()
            .mapToLong(Peer::getErc20TokenAmount)
            .max()
            .orElse(1L);
        
        Map<Peer, Double> recentBehaviorScores = calculateRecentBehaviorScores(peers, behaviorRepo);

        List<Peer> weightedList = new ArrayList<>();
        
        for (Peer peer : peers) 
        {
            double tokenScore = (double) peer.getErc20TokenAmount() / maxToken;
            double historicScore = calculateHistoricBehaviorScore(peer);
            double recentScore = recentBehaviorScores.getOrDefault(peer, 0.5);
            
            double compositeWeight = (tokenScore * TOKEN_WEIGHT) +
                                    (historicScore * HISTORIC_BEHAVIOR_WEIGHT) +
                                    (recentScore * RECENT_BEHAVIOR_WEIGHT);
            
            addWeightedEntries(weightedList, peer, compositeWeight);
        }

        return selectValidators(selected, weightedList, count);
    }

    public double calculateHistoricBehaviorScore(Peer peer) 
    {
        int total = peer.getRewardCounter() + peer.getMisbehaviorCounter();
        return total > 0 ? 
            (double) peer.getRewardCounter() / total : 
            0.5;
    }

    public Map<Peer, Double> calculateRecentBehaviorScores(List<Peer> peers, BehaviorHistoryRepository repo) 
    {
        Map<Peer, Double> scores = new HashMap<>();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(RECENT_DAYS);
        
        for (Peer peer : peers) 
        {
            List<BehaviorHistory> recentBehaviors = repo.findByPeerAndStatusUpdateAfter(peer, cutoff);
            
            long positiveCount = recentBehaviors.stream()
                .filter(BehaviorHistory::isPositive)
                .count();
            
            double score = recentBehaviors.isEmpty() ? 
                0.5 : (double) positiveCount / recentBehaviors.size();
            
            scores.put(peer, score);
        }
        
        return scores;
    }
    
    public double calculateRecentBehaviorScore(Peer peer, BehaviorHistoryRepository repo) 
    {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(RECENT_DAYS);
        
        List<BehaviorHistory> recentBehaviors = repo.findByPeerAndStatusUpdateAfter(peer, cutoff);
        
        long positiveCount = recentBehaviors.stream()
            .filter(BehaviorHistory::isPositive)
            .count();
        
        double score = recentBehaviors.isEmpty() ? 
            0.5 : (double) positiveCount / recentBehaviors.size();
        
        return score;
    }

    // Existing helper methods remain unchanged
    private void addWeightedEntries(List<Peer> list, Peer peer, double weight) 
    {
        int entries = (int) (weight * 1000) + 1;
        
        for (int i = 0; i < entries; i++) 
        {
            list.add(peer);
        }
    }

    private List<Peer> selectValidators(List<Peer> selected, List<Peer> candidates, int count) 
    {
        SecureRandom random = new SecureRandom();
        
        while (selected.size() < count && !candidates.isEmpty()) 
        {
            Peer chosen = candidates.get(random.nextInt(candidates.size()));
            
            if (!selected.contains(chosen)) 
            {
                selected.add(chosen);
                candidates.removeIf(p -> p.equals(chosen));
            }
        }
        
        return selected;
    }
}
