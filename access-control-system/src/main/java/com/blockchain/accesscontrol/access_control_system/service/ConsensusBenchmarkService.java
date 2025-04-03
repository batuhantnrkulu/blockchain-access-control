package com.blockchain.accesscontrol.access_control_system.service;

import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.BehaviorHistoryRepository;
import com.blockchain.accesscontrol.access_control_system.utils.ConsensusUtil;
import com.blockchain.accesscontrol.access_control_system.utils.GiniCalculator;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsensusBenchmarkService {

    private final MeterRegistry meterRegistry;
    private final ConsensusUtil consensusUtil;
    private final BehaviorHistoryRepository behaviorHistoryRepository;

    public ConsensusBenchmarkService(MeterRegistry meterRegistry, 
                                    ConsensusUtil consensusUtil, BehaviorHistoryRepository behaviorHistoryRepository) 
    {
        this.meterRegistry = meterRegistry;
        this.consensusUtil = consensusUtil;
        this.behaviorHistoryRepository = behaviorHistoryRepository;
    }

    public Map<String, Object> runComparisonBenchmark(List<Peer> network, int count) {
        Map<String, Object> results = new LinkedHashMap<>();
        
        // Our Algorithm
        List<Peer> ourValidators = consensusUtil.selectWeightedRandom(network, count, behaviorHistoryRepository);
        results.put("ourAlgorithm", calculateAllGinis(ourValidators));
        
        // Simulated Algorithms
        results.put("pureStake", calculateAllGinis(simulatePureStake(network, count)));
        results.put("pureRandom", calculateAllGinis(simulatePureRandom(network, count)));
        results.put("pureReputation", calculateAllGinis(simulatePureReputation(network, count)));
        
        // Record metrics
        recordGiniMetrics(results);
        
        return results;
    }

    private List<Peer> simulatePureStake(List<Peer> peers, int count) {
        return peers.stream()
            .sorted((a,b) -> Long.compare(b.getErc20TokenAmount(), a.getErc20TokenAmount()))
            .limit(count)
            .collect(Collectors.toList());
    }

    private List<Peer> simulatePureRandom(List<Peer> peers, int count) {
        Collections.shuffle(peers);
        return peers.stream().limit(count).collect(Collectors.toList());
    }

    private List<Peer> simulatePureReputation(List<Peer> peers, int count) {
        return peers.stream()
            .sorted((a,b) -> Double.compare(
                consensusUtil.calculateHistoricBehaviorScore(b),
                consensusUtil.calculateHistoricBehaviorScore(a)
            ))
            .limit(count)
            .collect(Collectors.toList());
    }

    public Map<String, Double> calculateAllGinis(List<Peer> validators) 
    {
    	if(validators == null || validators.isEmpty()) 
            return Map.of("token", 0d, "historic", 0d, "recent", 0d);
    	
        Map<String, List<Double>> components = new HashMap<>();
        
        validators.forEach(p -> {
            components.computeIfAbsent("token", k -> new ArrayList<>())
                     .add((double)p.getErc20TokenAmount());
            
            components.computeIfAbsent("historic", k -> new ArrayList<>())
                     .add(consensusUtil.calculateHistoricBehaviorScore(p));
            
            components.computeIfAbsent("recent", k -> new ArrayList<>())
                     .add(consensusUtil.calculateRecentBehaviorScore(p, behaviorHistoryRepository));
        });
        
        return components.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> GiniCalculator.calculateForDoubles(e.getValue())
            ));
    }

    private void recordGiniMetrics(Map<String, Object> results) {
        results.forEach((algorithm, data) -> {
            if(data instanceof Map) {
                ((Map<?,?>) data).forEach((k,v) -> {
                    String metricName = String.format("consensus.gini.%s.%s", algorithm, k);
                    meterRegistry.gauge(metricName, (Double)v);
                });
            }
        });
    }
}