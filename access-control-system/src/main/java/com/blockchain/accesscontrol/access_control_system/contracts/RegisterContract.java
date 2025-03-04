package com.blockchain.accesscontrol.access_control_system.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class RegisterContract extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f80fd5b5060405161092938038061092983398101604081905261002e916100f6565b816001600160a01b03811661005c57604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b6100658161008c565b50600180546001600160a01b0319166001600160a01b039290921691909117905550610127565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80516001600160a01b03811681146100f1575f80fd5b919050565b5f8060408385031215610107575f80fd5b610110836100db565b915061011e602084016100db565b90509250929050565b6107f5806101345f395ff3fe608060405234801561000f575f80fd5b506004361061009b575f3560e01c80638da5cb5b116100635780638da5cb5b1461014d578063c943dae51461015d578063e2dd13ca14610170578063f2fde38b14610193578063fb25798c146101a6575f80fd5b80631172a0301461009f57806339061ed8146100cf578063539752661461011d578063715018a61461013257806375ed28451461013a575b5f80fd5b6003546100b2906001600160a01b031681565b6040516001600160a01b0390911681526020015b60405180910390f35b6100b26100dd366004610628565b600260209081525f93845260408085208252928452919092208251808401830180519281529083019390920192909220919052546001600160a01b031681565b61013061012b366004610682565b6101b9565b005b6101306103c8565b6001546100b2906001600160a01b031681565b5f546001600160a01b03166100b2565b6100b261016b366004610628565b6103db565b61018361017e366004610628565b610433565b60405190151581526020016100c6565b6101306101a13660046106ed565b61048e565b6101306101b43660046106ed565b6104cb565b60018054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa158015610200573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906102249190610721565b60048111156102355761023561070d565b146102875760405162461bcd60e51b815260206004820152601b60248201527f4163636573732064656e6965643a204e6f7420616e2041646d696e000000000060448201526064015b60405180910390fd5b610292848484610433565b156103235760405162461bcd60e51b815260206004820152605560248201527f4572726f723a2041636365737320636f6e74726f6c20636f6e7472616374206160448201527f6c7265616479207265676973746572656420666f722074686973206f626a65636064820152743a161039bab13532b1ba161030b732103a3cb8329760591b608482015260a40161027e565b6001600160a01b038085165f90815260026020908152604080832093871683529290528190209051829190610359908590610761565b90815260405190819003602001812080546001600160a01b039384166001600160a01b0319909116179055848216918616907fbd6c61e8b971ac83726b8aa59829960d206a968b3bb14ffaf5460b63a32dbbf5906103ba908690869061077c565b60405180910390a350505050565b6103d06104f5565b6103d95f610521565b565b6001600160a01b038084165f90815260026020908152604080832093861683529290528181209151909190610411908490610761565b908152604051908190036020019020546001600160a01b031690509392505050565b6001600160a01b038381165f908152600260209081526040808320938616835292905281812091519091829161046a908590610761565b908152604051908190036020019020546001600160a01b0316141590509392505050565b6104966104f5565b6001600160a01b0381166104bf57604051631e4fbdf760e01b81525f600482015260240161027e565b6104c881610521565b50565b6104d36104f5565b600380546001600160a01b0319166001600160a01b0392909216919091179055565b5f546001600160a01b031633146103d95760405163118cdaa760e01b815233600482015260240161027e565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b80356001600160a01b0381168114610586575f80fd5b919050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f8301126105ae575f80fd5b813567ffffffffffffffff808211156105c9576105c961058b565b604051601f8301601f19908116603f011681019082821181831017156105f1576105f161058b565b81604052838152866020858801011115610609575f80fd5b836020870160208301375f602085830101528094505050505092915050565b5f805f6060848603121561063a575f80fd5b61064384610570565b925061065160208501610570565b9150604084013567ffffffffffffffff81111561066c575f80fd5b6106788682870161059f565b9150509250925092565b5f805f8060808587031215610695575f80fd5b61069e85610570565b93506106ac60208601610570565b9250604085013567ffffffffffffffff8111156106c7575f80fd5b6106d38782880161059f565b9250506106e260608601610570565b905092959194509250565b5f602082840312156106fd575f80fd5b61070682610570565b9392505050565b634e487b7160e01b5f52602160045260245ffd5b5f60208284031215610731575f80fd5b815160058110610706575f80fd5b5f5b83811015610759578181015183820152602001610741565b50505f910152565b5f825161077281846020870161073f565b9190910192915050565b604081525f835180604084015261079a81606085016020880161073f565b6001600160a01b0393909316602083015250601f91909101601f19160160600191905056fea2646970667358221220e2d1b5676e66d79e2d94d0abd0023f223db30a70e2acc2a8aa87fd921dd8caf064736f6c63430008150033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ACCESSCONTROLREGİSTRY = "accessControlRegistry";

    public static final String FUNC_JUDGECONTRACTADDRESS = "judgeContractAddress";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHİP = "renounceOwnership";

    public static final String FUNC_ROLEBASEDACCESSCONTRACT = "roleBasedAccessContract";

    public static final String FUNC_TRANSFEROWNERSHİP = "transferOwnership";

    public static final String FUNC_SETJUDGECONTRACTADDRESS = "setJudgeContractAddress";

    public static final String FUNC_REGİSTERACCESSCONTROLCONTRACT = "registerAccessControlContract";

    public static final String FUNC_GETACCESSCONTROLCONTRACT = "getAccessControlContract";

    public static final String FUNC_ACCESSCONTROLCONTRACTEXİSTS = "accessControlContractExists";

    public static final Event ACCESSCONTROLREGİSTERED_EVENT = new Event("AccessControlRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event OWNERSHİPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected RegisterContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RegisterContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RegisterContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RegisterContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AccessControlRegisteredEventResponse> getAccessControlRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSCONTROLREGİSTERED_EVENT, transactionReceipt);
        ArrayList<AccessControlRegisteredEventResponse> responses = new ArrayList<AccessControlRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessControlRegisteredEventResponse typedResponse = new AccessControlRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.objectAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.subjectAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.accType = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.contractAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessControlRegisteredEventResponse getAccessControlRegisteredEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSCONTROLREGİSTERED_EVENT, log);
        AccessControlRegisteredEventResponse typedResponse = new AccessControlRegisteredEventResponse();
        typedResponse.log = log;
        typedResponse.objectAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.subjectAddress = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.accType = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.contractAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccessControlRegisteredEventResponse> accessControlRegisteredEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessControlRegisteredEventFromLog(log));
    }

    public Flowable<AccessControlRegisteredEventResponse> accessControlRegisteredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSCONTROLREGİSTERED_EVENT));
        return accessControlRegisteredEventFlowable(filter);
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

    public RemoteFunctionCall<String> accessControlRegistry(String param0, String param1,
            String param2) {
        final Function function = new Function(FUNC_ACCESSCONTROLREGİSTRY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1), 
                new org.web3j.abi.datatypes.Utf8String(param2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> judgeContractAddress() {
        final Function function = new Function(FUNC_JUDGECONTRACTADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<TransactionReceipt> setJudgeContractAddress(
            String _judgeContractAddress) {
        final Function function = new Function(
                FUNC_SETJUDGECONTRACTADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _judgeContractAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerAccessControlContract(
            String objectAddress, String subjectAddress, String accType, String contractAddress) {
        final Function function = new Function(
                FUNC_REGİSTERACCESSCONTROLCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, objectAddress), 
                new org.web3j.abi.datatypes.Address(160, subjectAddress), 
                new org.web3j.abi.datatypes.Utf8String(accType), 
                new org.web3j.abi.datatypes.Address(160, contractAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getAccessControlContract(String objectAddress,
            String subjectAddress, String accType) {
        final Function function = new Function(FUNC_GETACCESSCONTROLCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, objectAddress), 
                new org.web3j.abi.datatypes.Address(160, subjectAddress), 
                new org.web3j.abi.datatypes.Utf8String(accType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> accessControlContractExists(String objectAddress,
            String subjectAddress, String accType) {
        final Function function = new Function(FUNC_ACCESSCONTROLCONTRACTEXİSTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, objectAddress), 
                new org.web3j.abi.datatypes.Address(160, subjectAddress), 
                new org.web3j.abi.datatypes.Utf8String(accType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static RegisterContract load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegisterContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RegisterContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegisterContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RegisterContract load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RegisterContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RegisterContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RegisterContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RegisterContract> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String initialAdmin,
            String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(RegisterContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<RegisterContract> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String initialAdmin, String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(RegisterContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RegisterContract> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String initialAdmin,
            String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(RegisterContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RegisterContract> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String initialAdmin, String _roleBasedAccessContract) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialAdmin), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedAccessContract)));
        return deployRemoteCall(RegisterContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class AccessControlRegisteredEventResponse extends BaseEventResponse {
        public String objectAddress;

        public String subjectAddress;

        public String accType;

        public String contractAddress;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
