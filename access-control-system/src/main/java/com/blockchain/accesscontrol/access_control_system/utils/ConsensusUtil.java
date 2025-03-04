package com.blockchain.accesscontrol.access_control_system.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.blockchain.accesscontrol.access_control_system.model.Peer;

@Component
public class ConsensusUtil 
{
	private final static SecureRandom random = new SecureRandom();

    public static List<Peer> selectWeightedRandom(List<Peer> peers, int count) 
    {
        List<Peer> selected = new ArrayList<>();

        // Create weighted list based on ERC20 token balance
        List<Peer> weightedList = new ArrayList<>();
        
        for (Peer peer : peers) 
        {
            int weight = (int) (peer.getErc20TokenAmount() / 10000); // Adjust weight scaling
            
            for (int i = 0; i < weight; i++) 
            {
                weightedList.add(peer);
            }
        }

        // Select random validators
        while (selected.size() < count && !weightedList.isEmpty()) 
        {
            Peer chosen = weightedList.get(random.nextInt(weightedList.size()));
        
            if (!selected.contains(chosen)) 
            {
                selected.add(chosen);
            }
        }

        return selected;
    }
}
