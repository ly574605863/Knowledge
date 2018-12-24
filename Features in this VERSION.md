### TPS and hierarchical network architecture
- 5K TPS: Verified transaction speeds in fully-encrypted real-world conditions on our test platform 
- Hierarchical network architecture: transaction throughput in the MATRIX network uses a distributed form of hierarchical load balancing for hash operations. The innovative dynamic layer generation mechanism gives the MATRIX blockchain great performance advantage    
### Transaction capabilities
- Deposits and Retracts: intelligent contracts enable miners and validators to manage deposits, retracts as well as refunds
- AI transactions: pose recognition, object and animal recognition
### Consensus
- Hybrid POW consensus mechanism: blocks are generated through qualifying validator nodes according to proof of stake (POS), and miner nodes who meet the computing requirements to solve for hash values（PoW） — providing computing security to the blockchain
- Tiered management for validator deposit accounts: optimized services for VIP account holders with higher possibility of being elected as top-level validators
- Delegation and re-election: A delegate cycle lasts for the creation of 300 blocks. During the current cycle, a lot of factors determine the final validators and miners for the next cycle from deposit list. The first block of each cycle will be jointly created by validator and miner nodes 
- Leader selection: Validators will in turn take the responsibility of Leader every block for transaction bundling. Meanwhile, other validators will vote. If consensus is not reached due to a problem with the validator or transaction, the block will not be created. In which case backup validators will cast a vote, the leader will be switched out, and once consensus is reached the replacement delegate takes over as leader to complete creation of the block. Information about the change of leaders can be viewed in the blockchain explorer
- Top-level uptime consensus: To prevent disruption or slowing of block creation due to downtime of miners and validator nodes, the validator will monitor the uptime status of miner and validator nodes, creating consensus about uptime status
- Top-level node update: Once consensus is reached about the uptime status of validator and mining nodes, a top-level downtime swapping algorithm will elect substitute miner and validator nodes as needed. Once consensus is reached, the substitute nodes will assume the work of the downtime nodes
- Top-level uptime services: top-level node uptime services receive block information and uptime status information and provide an interface to reference top-level uptime status. Once top-level validators are offline, they will assign the top-level nodes for the next round.  
- Broadcast blocks: Every 100 blocks comprise a broadcast round, with each hundredth block being a broadcast block. The broadcast block will contain some special transactions, such as random num votes transaction
- Stable operations after interruption and reboot: blocks will continue in a stable fashion after rebooting from a chain break
### Reward and Penalty mechanism
- Fixed block rewards: for top-level validator and miner nodes
- Interests of Deposits：interests are granted to miner and validator nodes for deposits. Interests are issued through a special account.
- Raffle rewards: A lottery reward will be automatically triggered during every 300 blocks, a winner will be awarded for sending transactions
- Transaction fee rewards: there will be set rewards connected with blockchain transaction fees
- Penalties: there will be penalties incurred according to uptime for prohibited behaviors

### Reward Calculation List

#### Block reward for miners 

- total: 0.2MAN/block

- miner for block generation:0.2*40%
- other miners:0.2* 50% * deposit ratio
- MATRIX foundation:0.2* 10% *deposit ratio


#### Block reward for validators 

- total: 2MAN/block

- leader for block generation:2*40%
- other validators:2*50%
- MATRIX foundation:2*10%


#### Interests of Deposits




