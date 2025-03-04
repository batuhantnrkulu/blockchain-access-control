package com.blockchain.accesscontrol.access_control_system.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.2.
 */
@SuppressWarnings("rawtypes")
public class JudgeContract extends Contract {
    public static final String BINARY = "0x6080604052620151806005556202a30060065561025860075534801562000024575f80fd5b5060405162001b9d38038062001b9d833981016040819052620000479162000581565b816001600160a01b0381166200007657604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b62000081816200049a565b50600280546001600160a01b038085166001600160a01b0319928316179092556001805492841692909116919091179055604080518082018252601b81527f556e617574686f72697a65642061636365737320617474656d7074000000000060208083019190915282518084018452600581526436b0b537b960d91b8183015283516060810185526123288152611b589281019290925261138893820193909352620001319290610e10620004e9565b604080518082018252601381527f546f6f206672657175656e74206163636573730000000000000000000000000060208083019190915282518084018452600581526436b4b737b960d91b8183015283516060810185526113888152610bb8928101929092526103e893820193909352620001b09290610708620004e9565b604080518082018252601481527f44617461206c65616b61676520617474656d707400000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552612af8815261232892810192909252611b58938201939093526200022f9290611c20620004e9565b604080518082018252601481527f50726976696c65676520657363616c6174696f6e00000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b8183015283516060810185526136b08152612ee09281019290925261271093820193909352620002ae9290614650620004e9565b604080518082018252601081526f14dc1bdbd99a5b99c8185d1d195b5c1d60821b60208083019190915282518084018452600581526436b4b737b960d91b8183015283516060810185526117708152610fa0928101929092526107d0938201939093526200032092906104b0620004e9565b604080518082018252601381527f5265707564696174696f6e20617474656d70740000000000000000000000000060208083019190915282518084018452600581526436b4b737b960d91b818301528351606081018552611b58815261138892810192909252610bb8938201939093526200039f9290610e10620004e9565b604080518082018252601381527f54616d706572696e67207769746820646174610000000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552612ee0815261271092810192909252611f40938201939093526200041e9290612a30620004e9565b604080518082018252601181527044656e69616c206f66205365727669636560781b60208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552615dc081526155f092810192909252614e20938201939093526200049292906202a300620004e9565b50506200074d565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f600485604051620004fc9190620005b7565b9081526040519081900360200190209050806200051a858262000685565b5060028181019290925582515f92835260019091016020908152604080842061ffff938416905590840151600384528184209083169055928301516004835292909120911690555050565b80516001600160a01b03811681146200057c575f80fd5b919050565b5f806040838503121562000593575f80fd5b6200059e8362000565565b9150620005ae6020840162000565565b90509250929050565b5f82515f5b81811015620005d85760208186018101518583015201620005bc565b505f920191825250919050565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806200060e57607f821691505b6020821081036200062d57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111562000680575f81815260208120601f850160051c810160208610156200065b5750805b601f850160051c820191505b818110156200067c5782815560010162000667565b5050505b505050565b81516001600160401b03811115620006a157620006a1620005e5565b620006b981620006b28454620005f9565b8462000633565b602080601f831160018114620006ef575f8415620006d75750858301515b5f19600386901b1c1916600185901b1785556200067c565b5f85815260208120601f198616915b828110156200071f57888601518255948401946001909101908401620006fe565b50858210156200073d57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b611442806200075b5f395ff3fe608060405234801561000f575f80fd5b50600436106100b1575f3560e01c80638da5cb5b1161006e5780638da5cb5b146101905780639334e69f146101a0578063b08aae46146101a9578063c4df01ce146101b2578063f2fde38b146101d4578063f851a440146101e7575f80fd5b80630e104278146100b55780631e349d1c146100f357806348d33ef214610114578063700733f81461011d578063715018a61461015b57806375ed284514610165575b5f80fd5b6100e06100c3366004610e16565b6001600160a01b03165f9081526003602052604090206002015490565b6040519081526020015b60405180910390f35b610106610101366004610e16565b6101fa565b6040516100ea929190610e85565b6100e060055481565b61014661012b366004610e16565b60036020525f90815260409020600181015460029091015482565b604080519283526020830191909152016100ea565b610163610593565b005b600154610178906001600160a01b031681565b6040516001600160a01b0390911681526020016100ea565b5f546001600160a01b0316610178565b6100e060075481565b6100e060065481565b6101c56101c0366004610f3a565b6105a6565b6040516100ea93929190610fc7565b6101636101e2366004610e16565b6108ae565b600254610178906001600160a01b031681565b5f606060018054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa158015610244573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906102689190611016565b600481111561027957610279610fef565b146102cb5760405162461bcd60e51b815260206004820152601b60248201527f4163636573732064656e6965643a204e6f7420616e2041646d696e000000000060448201526064015b60405180910390fd5b60015460405163156d12cb60e11b81526001600160a01b0385811660048301525f921690632ada2596906024015f60405180830381865afa158015610312573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f19168201604052610339919081019061107c565b90505f81608001514261034c919061116a565b604051652122a724a3a760d11b602082015290915060260160405160208183030381529060405280519060200120826060015160405160200161038f9190611183565b6040516020818303038152906040528051906020012003610417576005548110610412576103bc856108eb565b6103e585604051806040016040528060078152602001666e6f7468696e6760c81b815250610995565b611388604051806040016040528060068152602001652122a724a3a760d11b815250935093505050915091565b610563565b60405169535553504943494f555360b01b6020820152602a0160405160208183030381529060405280519060200120826060015160405160200161045b9190611183565b60405160208183030381529060405280519060200120036104d3576005548110610412576104a885604051806040016040528060078152602001666e6f7468696e6760c81b815250610995565b5f604051806040016040528060068152602001652122a724a3a760d11b815250935093505050915091565b604051684d414c4943494f555360b81b60208201526029016040516020818303038152906040528051906020012082606001516040516020016105169190611183565b6040516020818303038152906040528051906020012003610563576006548110610563576104a885604051806040016040528060078152602001666e6f7468696e6760c81b815250610995565b5f6040518060400160405280600b81526020016a1b9bdd0818da185b99d95960aa1b815250935093505050915091565b61059b610c65565b6105a45f610c91565b565b5f60608160018054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa1580156105f1573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906106159190611016565b600481111561062657610626610fef565b146106735760405162461bcd60e51b815260206004820152601b60248201527f4163636573732064656e6965643a204e6f7420616e2041646d696e000000000060448201526064016102c2565b5f8451116106bc5760405162461bcd60e51b8152602060048201526016602482015275526561736f6e2063616e6e6f7420626520656d70747960501b60448201526064016102c2565b6001600160a01b0385165f908152600360205260408082209051909142916004906106e8908990611183565b908152602001604051809103902090505f6004886040516107099190611183565b908152604051602091819003820190206002015485546001810187555f878152929092209092500161073b8982611224565b506001840183905581546107d7908a9084906107569061119e565b80601f01602080910402602001604051908101604052809291908181526020018280546107829061119e565b80156107cd5780601f106107a4576101008083540402835291602001916107cd565b820191905f5260205f20905b8154815290600101906020018083116107b057829003601f168201915b5050505050610995565b60018054604051634427673360e01b81526001600160a01b038c811660048301525f938601928492911690634427673390602401602060405180830381865afa158015610826573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061084a9190611016565b600481111561085b5761085b610fef565b600481111561086c5761086c610fef565b81526020019081526020015f205490506108878a828b610ce0565b8115610898576108988a838b610d90565b6002909401549399979850929695505050505050565b6108b6610c65565b6001600160a01b0381166108df57604051631e4fbdf760e01b81525f60048201526024016102c2565b6108e881610c91565b50565b600154604051633a36cb0f60e11b81526001600160a01b03838116600483015261138860248301529091169063746d961e906044015f604051808303815f87803b158015610937575f80fd5b505af1158015610949573d5f803e3d5ffd5b50505050806001600160a01b03167ff9b6ec770c96c3a6f9225bf3a8463a8ad03e884448af07b4ecd4d19d628946ee61138860405161098a91815260200190565b60405180910390a250565b6040516436b0b537b960d91b602082015260250160405160208183030381529060405280519060200120816040516020016109d09190611183565b6040516020818303038152906040528051906020012003610a8b57600154604051631bc15d5360e01b81526001600160a01b0390911690631bc15d5390610a1b9085906004016112e0565b5f604051808303815f87803b158015610a32575f80fd5b505af1158015610a44573d5f803e3d5ffd5b50505050816001600160a01b03167fa14bd4539d3435e61a25a7ed755caf1001aa7e9b044c46d2d004478fe2e88b77604051610a7f90611317565b60405180910390a25050565b6040516436b4b737b960d91b60208201526025016040516020818303038152906040528051906020012081604051602001610ac69190611183565b6040516020818303038152906040528051906020012003610b7557600154604051631bc15d5360e01b81526001600160a01b0390911690631bc15d5390610b1190859060040161133f565b5f604051808303815f87803b158015610b28575f80fd5b505af1158015610b3a573d5f803e3d5ffd5b50505050816001600160a01b03167fa14bd4539d3435e61a25a7ed755caf1001aa7e9b044c46d2d004478fe2e88b77604051610a7f90611377565b604051666e6f7468696e6760c81b60208201526027016040516020818303038152906040528051906020012081604051602001610bb29190611183565b6040516020818303038152906040528051906020012003610c6157600154604051631bc15d5360e01b81526001600160a01b0390911690631bc15d5390610bfd9085906004016113a0565b5f604051808303815f87803b158015610c14575f80fd5b505af1158015610c26573d5f803e3d5ffd5b50505050816001600160a01b03167fa14bd4539d3435e61a25a7ed755caf1001aa7e9b044c46d2d004478fe2e88b77604051610a7f906113d4565b5050565b5f546001600160a01b031633146105a45760405163118cdaa760e01b81523360048201526024016102c2565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b8115610d4857600154604051631daf405960e11b81526001600160a01b0385811660048301526024820185905290911690633b5e80b2906044015f604051808303815f87803b158015610d31575f80fd5b505af1158015610d43573d5f803e3d5ffd5b505050505b826001600160a01b03167f8c42d77a98c4fc8f88c68598c1aeb59aff4b2d81cdc6b9a467cebfb85893d7cc8383604051610d83929190610e85565b60405180910390a2505050565b6001600160a01b0383165f908152600360205260409020610db183426113f9565b600282018190556040516001600160a01b038616917fa85240da8b46fb556156b974826607837766e291b6badaffe434b30d4b4d1a3f91610df491908690610e85565b60405180910390a250505050565b6001600160a01b03811681146108e8575f80fd5b5f60208284031215610e26575f80fd5b8135610e3181610e02565b9392505050565b5f5b83811015610e52578181015183820152602001610e3a565b50505f910152565b5f8151808452610e71816020860160208601610e38565b601f01601f19169290920160200192915050565b828152604060208201525f610e9d6040830184610e5a565b949350505050565b634e487b7160e01b5f52604160045260245ffd5b60405160c0810167ffffffffffffffff81118282101715610edc57610edc610ea5565b60405290565b604051601f8201601f1916810167ffffffffffffffff81118282101715610f0b57610f0b610ea5565b604052919050565b5f67ffffffffffffffff821115610f2c57610f2c610ea5565b50601f01601f191660200190565b5f8060408385031215610f4b575f80fd5b8235610f5681610e02565b9150602083013567ffffffffffffffff811115610f71575f80fd5b8301601f81018513610f81575f80fd5b8035610f94610f8f82610f13565b610ee2565b818152866020838501011115610fa8575f80fd5b816020840160208301375f602083830101528093505050509250929050565b838152606060208201525f610fdf6060830185610e5a565b9050826040830152949350505050565b634e487b7160e01b5f52602160045260245ffd5b805160058110611011575f80fd5b919050565b5f60208284031215611026575f80fd5b610e3182611003565b805161101181610e02565b5f82601f830112611049575f80fd5b8151611057610f8f82610f13565b81815284602083860101111561106b575f80fd5b610e9d826020830160208701610e38565b5f6020828403121561108c575f80fd5b815167ffffffffffffffff808211156110a3575f80fd5b9083019060c082860312156110b6575f80fd5b6110be610eb9565b6110c78361102f565b81526020830151828111156110da575f80fd5b6110e68782860161103a565b6020830152506040830151828111156110fd575f80fd5b6111098782860161103a565b604083015250606083015182811115611120575f80fd5b61112c8782860161103a565b6060830152506080830151608082015261114860a08401611003565b60a082015295945050505050565b634e487b7160e01b5f52601160045260245ffd5b8181038181111561117d5761117d611156565b92915050565b5f8251611194818460208701610e38565b9190910192915050565b600181811c908216806111b257607f821691505b6020821081036111d057634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561121f575f81815260208120601f850160051c810160208610156111fc5750805b601f850160051c820191505b8181101561121b57828155600101611208565b5050505b505050565b815167ffffffffffffffff81111561123e5761123e610ea5565b6112528161124c845461119e565b846111d6565b602080601f831160018114611285575f841561126e5750858301515b5f19600386901b1c1916600185901b17855561121b565b5f85815260208120601f198616915b828110156112b357888601518255948401946001909101908401611294565b50858210156112d057878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6001600160a01b0382168152604060208201819052600990820152684d414c4943494f555360b81b60608201525f60808201610e31565b602081525f61117d6020830160098152684d414c4943494f555360b81b602082015260400190565b6001600160a01b0382168152604060208201819052600a9082015269535553504943494f555360b01b60608201525f60808201610e31565b602081525f61117d60208301600a815269535553504943494f555360b01b602082015260400190565b6001600160a01b0382168152604060208201819052600690820152652122a724a3a760d11b60608201525f60808201610e31565b602081525f61117d6020830160068152652122a724a3a760d11b602082015260400190565b8082018082111561117d5761117d61115656fea26469706673582212201996e0194a38f87d2671b2351fa134c742520231fd56a61f36338ea804265aa064736f6c63430008150033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADMİN = "admin";

    public static final String FUNC_BENİGNTHRESHOLD = "benignThreshold";

    public static final String FUNC_BENİGNTHRESHOLDFORMALİCİOUS = "benignThresholdForMalicious";

    public static final String FUNC_MİSBEHAVİORHİSTORY = "misbehaviorHistory";

    public static final String FUNC_MİSBEHAVİORTHRESHOLD = "misbehaviorThreshold";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHİP = "renounceOwnership";

    public static final String FUNC_ROLEBASEDACCESSCONTRACT = "roleBasedAccessContract";

    public static final String FUNC_TRANSFEROWNERSHİP = "transferOwnership";

    public static final String FUNC_REPORTMALİCİOUSACTİVİTY = "reportMaliciousActivity";

    public static final String FUNC_REPORTNONPENALİZEMİSBEHAVİOR = "reportNonPenalizeMisbehavior";

    public static final String FUNC_GETBLOCKİNGENDTİME = "getBlockingEndTime";

    public static final Event MEMBERBLOCKED_EVENT = new Event("MemberBlocked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event OWNERSHİPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event PENALİZED_EVENT = new Event("Penalized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event REWARDISSUED_EVENT = new Event("RewardIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event STATUSUPDATED_EVENT = new Event("StatusUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected JudgeContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected JudgeContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected JudgeContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected JudgeContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<MemberBlockedEventResponse> getMemberBlockedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERBLOCKED_EVENT, transactionReceipt);
        ArrayList<MemberBlockedEventResponse> responses = new ArrayList<MemberBlockedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MemberBlockedEventResponse typedResponse = new MemberBlockedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.blockingEndTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MemberBlockedEventResponse getMemberBlockedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MEMBERBLOCKED_EVENT, log);
        MemberBlockedEventResponse typedResponse = new MemberBlockedEventResponse();
        typedResponse.log = log;
        typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.blockingEndTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<MemberBlockedEventResponse> memberBlockedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMemberBlockedEventFromLog(log));
    }

    public Flowable<MemberBlockedEventResponse> memberBlockedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERBLOCKED_EVENT));
        return memberBlockedEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHİPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHİPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHİPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<PenalizedEventResponse> getPenalizedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PENALİZED_EVENT, transactionReceipt);
        ArrayList<PenalizedEventResponse> responses = new ArrayList<PenalizedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PenalizedEventResponse typedResponse = new PenalizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.penaltyAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PenalizedEventResponse getPenalizedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PENALİZED_EVENT, log);
        PenalizedEventResponse typedResponse = new PenalizedEventResponse();
        typedResponse.log = log;
        typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.penaltyAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<PenalizedEventResponse> penalizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPenalizedEventFromLog(log));
    }

    public Flowable<PenalizedEventResponse> penalizedEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PENALİZED_EVENT));
        return penalizedEventFlowable(filter);
    }

    public static List<RewardIssuedEventResponse> getRewardIssuedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REWARDISSUED_EVENT, transactionReceipt);
        ArrayList<RewardIssuedEventResponse> responses = new ArrayList<RewardIssuedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RewardIssuedEventResponse typedResponse = new RewardIssuedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.rewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RewardIssuedEventResponse getRewardIssuedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(REWARDISSUED_EVENT, log);
        RewardIssuedEventResponse typedResponse = new RewardIssuedEventResponse();
        typedResponse.log = log;
        typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.rewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<RewardIssuedEventResponse> rewardIssuedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRewardIssuedEventFromLog(log));
    }

    public Flowable<RewardIssuedEventResponse> rewardIssuedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REWARDISSUED_EVENT));
        return rewardIssuedEventFlowable(filter);
    }

    public static List<StatusUpdatedEventResponse> getStatusUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(STATUSUPDATED_EVENT, transactionReceipt);
        ArrayList<StatusUpdatedEventResponse> responses = new ArrayList<StatusUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StatusUpdatedEventResponse typedResponse = new StatusUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newStatus = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static StatusUpdatedEventResponse getStatusUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(STATUSUPDATED_EVENT, log);
        StatusUpdatedEventResponse typedResponse = new StatusUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.memberAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newStatus = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<StatusUpdatedEventResponse> statusUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getStatusUpdatedEventFromLog(log));
    }

    public Flowable<StatusUpdatedEventResponse> statusUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STATUSUPDATED_EVENT));
        return statusUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> admin() {
        final Function function = new Function(FUNC_ADMİN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> benignThreshold() {
        final Function function = new Function(FUNC_BENİGNTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> benignThresholdForMalicious() {
        final Function function = new Function(FUNC_BENİGNTHRESHOLDFORMALİCİOUS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> misbehaviorHistory(String param0) {
        final Function function = new Function(FUNC_MİSBEHAVİORHİSTORY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> misbehaviorThreshold() {
        final Function function = new Function(FUNC_MİSBEHAVİORTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHİP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> roleBasedAccessContract() {
        final Function function = new Function(FUNC_ROLEBASEDACCESSCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHİP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reportMaliciousActivity(String _memberAddress,
            String reason) {
        final Function function = new Function(
                FUNC_REPORTMALİCİOUSACTİVİTY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress), 
                new org.web3j.abi.datatypes.Utf8String(reason)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reportNonPenalizeMisbehavior(
            String _memberAddress) {
        final Function function = new Function(
                FUNC_REPORTNONPENALİZEMİSBEHAVİOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getBlockingEndTime(String _memberAddress) {
        final Function function = new Function(FUNC_GETBLOCKİNGENDTİME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static JudgeContract load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new JudgeContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static JudgeContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new JudgeContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static JudgeContract load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new JudgeContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static JudgeContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new JudgeContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<JudgeContract> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String initialAdmin,
            String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(JudgeContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<JudgeContract> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String initialAdmin, String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(JudgeContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<JudgeContract> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String initialAdmin,
            String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(JudgeContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<JudgeContract> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String initialAdmin, String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(JudgeContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class MemberBlockedEventResponse extends BaseEventResponse {
        public String memberAddress;

        public BigInteger blockingEndTime;

        public String reason;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class PenalizedEventResponse extends BaseEventResponse {
        public String memberAddress;

        public BigInteger penaltyAmount;

        public String reason;
    }

    public static class RewardIssuedEventResponse extends BaseEventResponse {
        public String memberAddress;

        public BigInteger rewardAmount;
    }

    public static class StatusUpdatedEventResponse extends BaseEventResponse {
        public String memberAddress;

        public String newStatus;
    }
}
