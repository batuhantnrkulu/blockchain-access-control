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
    public static final String BINARY = "0x6080604052620151806005556202a30060065561025860075534801562000024575f80fd5b5060405162001a1238038062001a12833981016040819052620000479162000581565b816001600160a01b0381166200007657604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b62000081816200049a565b50600280546001600160a01b038085166001600160a01b0319928316179092556001805492841692909116919091179055604080518082018252601b81527f556e617574686f72697a65642061636365737320617474656d7074000000000060208083019190915282518084018452600581526436b0b537b960d91b8183015283516060810185526123288152611b589281019290925261138893820193909352620001319290610e10620004e9565b604080518082018252601381527f546f6f206672657175656e74206163636573730000000000000000000000000060208083019190915282518084018452600581526436b4b737b960d91b8183015283516060810185526113888152610bb8928101929092526103e893820193909352620001b09290610708620004e9565b604080518082018252601481527f44617461206c65616b61676520617474656d707400000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552612af8815261232892810192909252611b58938201939093526200022f9290611c20620004e9565b604080518082018252601481527f50726976696c65676520657363616c6174696f6e00000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b8183015283516060810185526136b08152612ee09281019290925261271093820193909352620002ae9290614650620004e9565b604080518082018252601081526f14dc1bdbd99a5b99c8185d1d195b5c1d60821b60208083019190915282518084018452600581526436b4b737b960d91b8183015283516060810185526117708152610fa0928101929092526107d0938201939093526200032092906104b0620004e9565b604080518082018252601381527f5265707564696174696f6e20617474656d70740000000000000000000000000060208083019190915282518084018452600581526436b4b737b960d91b818301528351606081018552611b58815261138892810192909252610bb8938201939093526200039f9290610e10620004e9565b604080518082018252601381527f54616d706572696e67207769746820646174610000000000000000000000000060208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552612ee0815261271092810192909252611f40938201939093526200041e9290612a30620004e9565b604080518082018252601181527044656e69616c206f66205365727669636560781b60208083019190915282518084018452600581526436b0b537b960d91b818301528351606081018552615dc081526155f092810192909252614e20938201939093526200049292906202a300620004e9565b50506200074d565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f600485604051620004fc9190620005b7565b9081526040519081900360200190209050806200051a858262000685565b5060028181019290925582515f92835260019091016020908152604080842061ffff938416905590840151600384528184209083169055928301516004835292909120911690555050565b80516001600160a01b03811681146200057c575f80fd5b919050565b5f806040838503121562000593575f80fd5b6200059e8362000565565b9150620005ae6020840162000565565b90509250929050565b5f82515f5b81811015620005d85760208186018101518583015201620005bc565b505f920191825250919050565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806200060e57607f821691505b6020821081036200062d57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111562000680575f81815260208120601f850160051c810160208610156200065b5750805b601f850160051c820191505b818110156200067c5782815560010162000667565b5050505b505050565b81516001600160401b03811115620006a157620006a1620005e5565b620006b981620006b28454620005f9565b8462000633565b602080601f831160018114620006ef575f8415620006d75750858301515b5f19600386901b1c1916600185901b1785556200067c565b5f85815260208120601f198616915b828110156200071f57888601518255948401946001909101908401620006fe565b50858210156200073d57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6112b7806200075b5f395ff3fe608060405234801561000f575f80fd5b50600436106100b1575f3560e01c80638da5cb5b1161006e5780638da5cb5b146101905780639334e69f146101a0578063b08aae46146101a9578063c4df01ce146101b2578063f2fde38b146101d5578063f851a440146101e8575f80fd5b80630e104278146100b55780631e349d1c146100f357806348d33ef214610114578063700733f81461011d578063715018a61461015b57806375ed284514610165575b5f80fd5b6100e06100c3366004610d17565b6001600160a01b03165f9081526003602052604090206002015490565b6040519081526020015b60405180910390f35b610106610101366004610d17565b6101fb565b6040516100ea929190610d86565b6100e060055481565b61014661012b366004610d17565b60036020525f90815260409020600181015460029091015482565b604080519283526020830191909152016100ea565b61016361056a565b005b600154610178906001600160a01b031681565b6040516001600160a01b0390911681526020016100ea565b5f546001600160a01b0316610178565b6100e060075481565b6100e060065481565b6101c56101c0366004610e3b565b61057d565b6040516100ea9493929190610ec8565b6101636101e3366004610d17565b61085c565b600254610178906001600160a01b031681565b5f606060018054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa158015610245573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906102699190610f2a565b600481111561027a5761027a610f03565b146102a05760405162461bcd60e51b815260040161029790610f43565b60405180910390fd5b60015460405163156d12cb60e11b81526001600160a01b0385811660048301525f921690632ada2596906024015f60405180830381865afa1580156102e7573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f1916820160405261030e9190810190610fdb565b90505f81608001514261032191906110c9565b604051652122a724a3a760d11b602082015290915060260160405160208183030381529060405280519060200120826060015160405160200161036491906110dc565b60405160208183030381529060405280519060200120036103ed5760055481106103e85761039185610899565b6103ba85604051806040016040528060078152602001666e6f7468696e6760c81b815250610943565b50611388604051806040016040528060068152602001652122a724a3a760d11b815250935093505050915091565b61053a565b60405169535553504943494f555360b01b6020820152602a0160405160208183030381529060405280519060200120826060015160405160200161043191906110dc565b60405160208183030381529060405280519060200120036104aa5760055481106103e85761047e85604051806040016040528060078152602001666e6f7468696e6760c81b815250610943565b505f604051806040016040528060068152602001652122a724a3a760d11b815250935093505050915091565b604051684d414c4943494f555360b81b60208201526029016040516020818303038152906040528051906020012082606001516040516020016104ed91906110dc565b604051602081830303815290604052805190602001200361053a57600654811061053a5761047e85604051806040016040528060078152602001666e6f7468696e6760c81b815250610943565b5f6040518060400160405280600b81526020016a1b9bdd0818da185b99d95960aa1b815250935093505050915091565b610572610b66565b61057b5f610b92565b565b5f6060818160018054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa1580156105c9573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906105ed9190610f2a565b60048111156105fe576105fe610f03565b1461061b5760405162461bcd60e51b815260040161029790610f43565b5f8551116106645760405162461bcd60e51b8152602060048201526016602482015275526561736f6e2063616e6e6f7420626520656d70747960501b6044820152606401610297565b6001600160a01b0386165f90815260036020526040808220905190914291600490610690908a906110dc565b908152602001604051809103902090505f6004896040516106b191906110dc565b908152604051602091819003820190206002015485546001810187555f87815292909220909250016106e38a8261117d565b5060018401839055815461077f908b9084906106fe906110f7565b80601f016020809104026020016040519081016040528092919081815260200182805461072a906110f7565b80156107755780601f1061074c57610100808354040283529160200191610775565b820191905f5260205f20905b81548152906001019060200180831161075857829003601f168201915b5050505050610943565b60018054604051634427673360e01b81526001600160a01b038e811660048301529398505f939286019284921690634427673390602401602060405180830381865afa1580156107d1573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906107f59190610f2a565b600481111561080657610806610f03565b600481111561081757610817610f03565b81526020019081526020015f205490506108328b828c610be1565b8115610843576108438b838c610c91565b600290940154939a989950929793965092945050505050565b610864610b66565b6001600160a01b03811661088d57604051631e4fbdf760e01b81525f6004820152602401610297565b61089681610b92565b50565b600154604051633a36cb0f60e11b81526001600160a01b03838116600483015261138860248301529091169063746d961e906044015f604051808303815f87803b1580156108e5575f80fd5b505af11580156108f7573d5f803e3d5ffd5b50505050806001600160a01b03167ff9b6ec770c96c3a6f9225bf3a8463a8ad03e884448af07b4ecd4d19d628946ee61138860405161093891815260200190565b60405180910390a250565b606080604051602001610961906436b0b537b960d91b815260050190565b604051602081830303815290604052805190602001208360405160200161098891906110dc565b60405160208183030381529060405280519060200120036109c957506040805180820190915260098152684d414c4943494f555360b81b6020820152610abd565b6040516436b4b737b960d91b60208201526025016040516020818303038152906040528051906020012083604051602001610a0491906110dc565b6040516020818303038152906040528051906020012003610a46575060408051808201909152600a815269535553504943494f555360b01b6020820152610abd565b604051666e6f7468696e6760c81b60208201526027016040516020818303038152906040528051906020012083604051602001610a8391906110dc565b6040516020818303038152906040528051906020012003610abd57506040805180820190915260068152652122a724a3a760d11b60208201525b600154604051631bc15d5360e01b81526001600160a01b0390911690631bc15d5390610aef9087908590600401611239565b5f604051808303815f87803b158015610b06575f80fd5b505af1158015610b18573d5f803e3d5ffd5b50505050836001600160a01b03167fa14bd4539d3435e61a25a7ed755caf1001aa7e9b044c46d2d004478fe2e88b7782604051610b55919061125c565b60405180910390a290505b92915050565b5f546001600160a01b0316331461057b5760405163118cdaa760e01b8152336004820152602401610297565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b8115610c4957600154604051631daf405960e11b81526001600160a01b0385811660048301526024820185905290911690633b5e80b2906044015f604051808303815f87803b158015610c32575f80fd5b505af1158015610c44573d5f803e3d5ffd5b505050505b826001600160a01b03167f8c42d77a98c4fc8f88c68598c1aeb59aff4b2d81cdc6b9a467cebfb85893d7cc8383604051610c84929190610d86565b60405180910390a2505050565b6001600160a01b0383165f908152600360205260409020610cb2834261126e565b600282018190556040516001600160a01b038616917fa85240da8b46fb556156b974826607837766e291b6badaffe434b30d4b4d1a3f91610cf591908690610d86565b60405180910390a250505050565b6001600160a01b0381168114610896575f80fd5b5f60208284031215610d27575f80fd5b8135610d3281610d03565b9392505050565b5f5b83811015610d53578181015183820152602001610d3b565b50505f910152565b5f8151808452610d72816020860160208601610d39565b601f01601f19169290920160200192915050565b828152604060208201525f610d9e6040830184610d5b565b949350505050565b634e487b7160e01b5f52604160045260245ffd5b60405160c0810167ffffffffffffffff81118282101715610ddd57610ddd610da6565b60405290565b604051601f8201601f1916810167ffffffffffffffff81118282101715610e0c57610e0c610da6565b604052919050565b5f67ffffffffffffffff821115610e2d57610e2d610da6565b50601f01601f191660200190565b5f8060408385031215610e4c575f80fd5b8235610e5781610d03565b9150602083013567ffffffffffffffff811115610e72575f80fd5b8301601f81018513610e82575f80fd5b8035610e95610e9082610e14565b610de3565b818152866020838501011115610ea9575f80fd5b816020840160208301375f602083830101528093505050509250929050565b848152608060208201525f610ee06080830186610d5b565b8460408401528281036060840152610ef88185610d5b565b979650505050505050565b634e487b7160e01b5f52602160045260245ffd5b805160058110610f25575f80fd5b919050565b5f60208284031215610f3a575f80fd5b610d3282610f17565b6020808252602b908201527f4163636573732064656e6965643a204e6f7420616e2041646d696e202d204a7560408201526a1919d950dbdb9d1c9858dd60aa1b606082015260800190565b8051610f2581610d03565b5f82601f830112610fa8575f80fd5b8151610fb6610e9082610e14565b818152846020838601011115610fca575f80fd5b610d9e826020830160208701610d39565b5f60208284031215610feb575f80fd5b815167ffffffffffffffff80821115611002575f80fd5b9083019060c08286031215611015575f80fd5b61101d610dba565b61102683610f8e565b8152602083015182811115611039575f80fd5b61104587828601610f99565b60208301525060408301518281111561105c575f80fd5b61106887828601610f99565b60408301525060608301518281111561107f575f80fd5b61108b87828601610f99565b606083015250608083015160808201526110a760a08401610f17565b60a082015295945050505050565b634e487b7160e01b5f52601160045260245ffd5b81810381811115610b6057610b606110b5565b5f82516110ed818460208701610d39565b9190910192915050565b600181811c9082168061110b57607f821691505b60208210810361112957634e487b7160e01b5f52602260045260245ffd5b50919050565b601f821115611178575f81815260208120601f850160051c810160208610156111555750805b601f850160051c820191505b8181101561117457828155600101611161565b5050505b505050565b815167ffffffffffffffff81111561119757611197610da6565b6111ab816111a584546110f7565b8461112f565b602080601f8311600181146111de575f84156111c75750858301515b5f19600386901b1c1916600185901b178555611174565b5f85815260208120601f198616915b8281101561120c578886015182559484019460019091019084016111ed565b508582101561122957878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6001600160a01b03831681526040602082018190525f90610d9e90830184610d5b565b602081525f610d326020830184610d5b565b80820180821115610b6057610b606110b556fea264697066735822122052b9ee0faadb6572ed08140e88ce620ded1bf5987ec1a6e021a3ea959be80b4d64736f6c63430008150033";

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
