package com.blockchain.accesscontrol.access_control_system.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
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
public class RoleBasedAccessControl extends Contract {
    public static final String BINARY = "0x608060405234801562000010575f80fd5b5060405162001c3838038062001c38833981016040819052620000339162000080565b5f80546001600160a01b039384166001600160a01b03199182161790915560018054929093169116179055620000b6565b80516001600160a01b03811681146200007b575f80fd5b919050565b5f806040838503121562000092575f80fd5b6200009d8362000064565b9150620000ad6020840162000064565b90509250929050565b611b7480620000c45f395ff3fe608060405234801561000f575f80fd5b50600436106100fb575f3560e01c8063746d961e1161009357806380e52e3f1161006357806380e52e3f14610235578063d3a2ab1014610248578063deb2bbef1461025b578063f851a4401461027e575f80fd5b8063746d961e146101dd5780637575bb42146101f05780637a0c2647146101f85780637e7a06e614610222575f80fd5b80633b5e80b2116100ce5780633b5e80b21461018457806344276733146101975780636925da90146101b75780636fab4804146101ca575f80fd5b806311192999146100ff5780631816faff146101285780631bc15d531461014f5780632ada259614610164575b5f80fd5b61011261010d366004611439565b610291565b60405161011f9190611574565b60405180910390f35b6101306103ac565b604080516001600160a01b03909316835260208301919091520161011f565b61016261015d3660046115eb565b610424565b005b610177610172366004611638565b61051e565b60405161011f919061165a565b61016261019236600461166c565b61062b565b6101aa6101a5366004611638565b6106fb565b60405161011f9190611696565b6101626101c53660046116b0565b6107fe565b6101626101d83660046116e7565b6108dd565b6101626101eb36600461166c565b610a31565b610112610b01565b5f5461020a906001600160a01b031681565b6040516001600160a01b03909116815260200161011f565b61020a610230366004611638565b610c8d565b610162610243366004611638565b610d90565b610162610256366004611713565b610f07565b61026e6102693660046116e7565b611004565b604051901515815260200161011f565b60015461020a906001600160a01b031681565b60605f8054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa1580156102d9573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906102fd919061178f565b600481111561030e5761030e6114c0565b036103345760405162461bcd60e51b815260040161032b906117aa565b60405180910390fd5b5f54604051631119299960e01b81526001600160a01b03909116906311192999906103639085906004016117e1565b5f60405180830381865afa15801561037d573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526103a491908101906118fb565b90505b919050565b5f80546040516370a0823160e01b81523360048201819052839290916001600160a01b03909116906370a0823190602401602060405180830381865afa1580156103f8573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061041c91906119b8565b915091509091565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa15801561046a573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061048e919061178f565b600481111561049f5761049f6114c0565b146104bc5760405162461bcd60e51b815260040161032b906119cf565b5f54604051631bc15d5360e01b81526001600160a01b0390911690631bc15d53906104ed9085908590600401611a23565b5f604051808303815f87803b158015610504575f80fd5b505af1158015610516573d5f803e3d5ffd5b505050505050565b610526611306565b5f8054604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa15801561056c573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610590919061178f565b60048111156105a1576105a16114c0565b036105be5760405162461bcd60e51b815260040161032b906117aa565b5f5460405163156d12cb60e11b81526001600160a01b03848116600483015290911690632ada2596906024015f60405180830381865afa158015610604573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526103a49190810190611a46565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa158015610671573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610695919061178f565b60048111156106a6576106a66114c0565b146106c35760405162461bcd60e51b815260040161032b906119cf565b5f54604051631daf405960e11b81526001600160a01b0384811660048301526024820184905290911690633b5e80b2906044016104ed565b5f8054604051634427673360e01b815233600482015282916001600160a01b031690634427673390602401602060405180830381865afa158015610741573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610765919061178f565b6004811115610776576107766114c0565b036107935760405162461bcd60e51b815260040161032b906117aa565b5f54604051634427673360e01b81526001600160a01b03848116600483015290911690634427673390602401602060405180830381865afa1580156107da573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906103a4919061178f565b806004811115610810576108106114c0565b5f54604051634427673360e01b81526001600160a01b03858116600483015290911690634427673390602401602060405180830381865afa158015610857573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061087b919061178f565b600481111561088c5761088c6114c0565b146108d95760405162461bcd60e51b815260206004820152601d60248201527f4163636573732064656e6965643a20696e636f727265637420726f6c65000000604482015260640161032b565b5050565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa158015610923573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610947919061178f565b6004811115610958576109586114c0565b14806109dc57505f54604051634427673360e01b81523360048201526002916001600160a01b031690634427673390602401602060405180830381865afa1580156109a5573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906109c9919061178f565b60048111156109da576109da6114c0565b145b6109f85760405162461bcd60e51b815260040161032b90611a78565b5f54604051631bead20160e21b81526001600160a01b038481166004830152838116602483015290911690636fab4804906044016104ed565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa158015610a77573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610a9b919061178f565b6004811115610aac57610aac6114c0565b14610ac95760405162461bcd60e51b815260040161032b906119cf565b5f54604051633a36cb0f60e11b81526001600160a01b038481166004830152602482018490529091169063746d961e906044016104ed565b606060015f54604051634427673360e01b81523360048201526001600160a01b0390911690634427673390602401602060405180830381865afa158015610b4a573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610b6e919061178f565b6004811115610b7f57610b7f6114c0565b1480610c0357505f54604051634427673360e01b81523360048201526002916001600160a01b031690634427673390602401602060405180830381865afa158015610bcc573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610bf0919061178f565b6004811115610c0157610c016114c0565b145b610c1f5760405162461bcd60e51b815260040161032b90611a78565b5f805460408051633abadda160e11b815290516001600160a01b0390921692637575bb42926004808401938290030181865afa158015610c61573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f19168201604052610c8891908101906118fb565b905090565b5f8054604051634427673360e01b815233600482015282916001600160a01b031690634427673390602401602060405180830381865afa158015610cd3573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610cf7919061178f565b6004811115610d0857610d086114c0565b03610d255760405162461bcd60e51b815260040161032b906117aa565b5f54604051633f3d037360e11b81526001600160a01b03848116600483015290911690637e7a06e690602401602060405180830381865afa158015610d6c573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906103a49190611ac9565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa158015610dd6573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610dfa919061178f565b6004811115610e0b57610e0b6114c0565b1480610e8f57505f54604051634427673360e01b81523360048201526002916001600160a01b031690634427673390602401602060405180830381865afa158015610e58573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610e7c919061178f565b6004811115610e8d57610e8d6114c0565b145b610eab5760405162461bcd60e51b815260040161032b90611a78565b5f546040516380e52e3f60e01b81526001600160a01b038381166004830152909116906380e52e3f906024015f604051808303815f87803b158015610eee575f80fd5b505af1158015610f00573d5f803e3d5ffd5b5050505050565b5f54604051634427673360e01b81523360048201526001916001600160a01b031690634427673390602401602060405180830381865afa158015610f4d573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610f71919061178f565b6004811115610f8257610f826114c0565b14610f9f5760405162461bcd60e51b815260040161032b906119cf565b5f54604051630d3a2ab160e41b81526001600160a01b039091169063d3a2ab1090610fd290869086908690600401611ae4565b5f604051808303815f87803b158015610fe9575f80fd5b505af1158015610ffb573d5f803e3d5ffd5b50505050505050565b5f8054604051634427673360e01b815233600482015282916001600160a01b031690634427673390602401602060405180830381865afa15801561104a573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061106e919061178f565b600481111561107f5761107f6114c0565b0361109c5760405162461bcd60e51b815260040161032b906117aa565b5f8054604051634427673360e01b81526001600160a01b03868116600483015290911690634427673390602401602060405180830381865afa1580156110e4573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611108919061178f565b5f8054604051634427673360e01b81526001600160a01b03878116600483015293945091921690634427673390602401602060405180830381865afa158015611153573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611177919061178f565b90505f600283600481111561118e5761118e6114c0565b1480156111ac575060028260048111156111aa576111aa6114c0565b145b5f805460405163156d12cb60e11b81526001600160a01b038a8116600483015293945091921690632ada2596906024015f60405180830381865afa1580156111f6573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f1916820160405261121d9190810190611a46565b6040908101515f8054925163156d12cb60e11b81526001600160a01b038a8116600483015292945090929190911690632ada2596906024015f60405180830381865afa15801561126f573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526112969190810190611a46565b6040015190505f816040516020016112ae9190611b23565b60405160208183030381529060405280519060200120836040516020016112d59190611b23565b6040516020818303038152906040528051906020012014905083806112f75750805b96505050505050505b92915050565b6040518060c001604052805f6001600160a01b031681526020016060815260200160608152602001606081526020015f81526020015f600481111561134d5761134d6114c0565b905290565b634e487b7160e01b5f52604160045260245ffd5b60405160c0810167ffffffffffffffff8111828210171561138957611389611352565b60405290565b604051601f8201601f1916810167ffffffffffffffff811182821017156113b8576113b8611352565b604052919050565b5f67ffffffffffffffff8211156113d9576113d9611352565b50601f01601f191660200190565b5f82601f8301126113f6575f80fd5b8135611409611404826113c0565b61138f565b81815284602083860101111561141d575f80fd5b816020850160208301375f918101602001919091529392505050565b5f60208284031215611449575f80fd5b813567ffffffffffffffff81111561145f575f80fd5b61146b848285016113e7565b949350505050565b5f5b8381101561148d578181015183820152602001611475565b50505f910152565b5f81518084526114ac816020860160208601611473565b601f01601f19169290920160200192915050565b634e487b7160e01b5f52602160045260245ffd5b600581106114f057634e487b7160e01b5f52602160045260245ffd5b9052565b60018060a01b0381511682525f602082015160c0602085015261151a60c0850182611495565b9050604083015184820360408601526115338282611495565b9150506060830151848203606086015261154d8282611495565b9150506080830151608085015260a083015161156c60a08601826114d4565b509392505050565b5f602080830181845280855180835260408601915060408160051b87010192508387015f5b828110156115c757603f198886030184526115b58583516114f4565b94509285019290850190600101611599565b5092979650505050505050565b6001600160a01b03811681146115e8575f80fd5b50565b5f80604083850312156115fc575f80fd5b8235611607816115d4565b9150602083013567ffffffffffffffff811115611622575f80fd5b61162e858286016113e7565b9150509250929050565b5f60208284031215611648575f80fd5b8135611653816115d4565b9392505050565b602081525f61165360208301846114f4565b5f806040838503121561167d575f80fd5b8235611688816115d4565b946020939093013593505050565b6020810161130082846114d4565b600581106115e8575f80fd5b5f80604083850312156116c1575f80fd5b82356116cc816115d4565b915060208301356116dc816116a4565b809150509250929050565b5f80604083850312156116f8575f80fd5b8235611703816115d4565b915060208301356116dc816115d4565b5f805f60608486031215611725575f80fd5b8335611730816115d4565b9250602084013567ffffffffffffffff8082111561174c575f80fd5b611758878388016113e7565b9350604086013591508082111561176d575f80fd5b5061177a868287016113e7565b9150509250925092565b80516103a7816116a4565b5f6020828403121561179f575f80fd5b8151611653816116a4565b6020808252601b908201527f4163636573732064656e6965643a204e6f742061204d656d6265720000000000604082015260600190565b602081525f6116536020830184611495565b80516103a7816115d4565b5f82601f83011261180d575f80fd5b815161181b611404826113c0565b81815284602083860101111561182f575f80fd5b61146b826020830160208701611473565b5f60c08284031215611850575f80fd5b611858611366565b9050611863826117f3565b8152602082015167ffffffffffffffff8082111561187f575f80fd5b61188b858386016117fe565b602084015260408401519150808211156118a3575f80fd5b6118af858386016117fe565b604084015260608401519150808211156118c7575f80fd5b506118d4848285016117fe565b606083015250608082015160808201526118f060a08301611784565b60a082015292915050565b5f602080838503121561190c575f80fd5b825167ffffffffffffffff80821115611923575f80fd5b818501915085601f830112611936575f80fd5b81518181111561194857611948611352565b8060051b61195785820161138f565b9182528381018501918581019089841115611970575f80fd5b86860192505b838310156119ab5782518581111561198d575f8081fd5b61199b8b89838a0101611840565b8352509186019190860190611976565b9998505050505050505050565b5f602082840312156119c8575f80fd5b5051919050565b60208082526034908201527f4163636573732064656e6965643a204e6f7420616e2041646d696e202d20526f6040820152731b1950985cd9591058d8d95cdcd0dbdb9d1c9bdb60621b606082015260800190565b6001600160a01b03831681526040602082018190525f9061146b90830184611495565b5f60208284031215611a56575f80fd5b815167ffffffffffffffff811115611a6c575f80fd5b61146b84828501611840565b60208082526031908201527f4163636573732064656e6965643a204e6f7420616e2041646d696e206f7220506040820152701c9a5b585c9e4811dc9bdd5c0812195859607a1b606082015260800190565b5f60208284031215611ad9575f80fd5b8151611653816115d4565b6001600160a01b03841681526060602082018190525f90611b0790830185611495565b8281036040840152611b198185611495565b9695505050505050565b5f8251611b34818460208701611473565b919091019291505056fea2646970667358221220812b1ba297de85675cbd80f2e3f7604942c7a86ba62bc2b6286374ab5d91ed8f64736f6c63430008150033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADMİN = "admin";

    public static final String FUNC_ROLETOKENCONTRACT = "roleTokenContract";

    public static final String FUNC_PRİNTBALANCES = "printBalances";

    public static final String FUNC_REQUİREROLE = "requireRole";

    public static final String FUNC_ASSİGNROLE = "assignRole";

    public static final String FUNC_SWAPROLE = "swapRole";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_GETROLE = "getRole";

    public static final String FUNC_GETMEMBER = "getMember";

    public static final String FUNC_ARESAMEORGLOBALMEMBERS = "areSameOrGlobalMembers";

    public static final String FUNC_GETLOCALRESOURCETABLE = "getLocalResourceTable";

    public static final String FUNC_GETGLOBALRESOURCETABLE = "getGlobalResourceTable";

    public static final String FUNC_GETPRİMARYHEADADDRESS = "getPrimaryHeadAddress";

    public static final String FUNC_UPDATEMEMBERSTATUS = "updateMemberStatus";

    public static final String FUNC_PENALİZEMEMBER = "penalizeMember";

    public static final String FUNC_REWARDMEMBER = "rewardMember";

    @Deprecated
    protected RoleBasedAccessControl(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RoleBasedAccessControl(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RoleBasedAccessControl(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RoleBasedAccessControl(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> admin() {
        final Function function = new Function(FUNC_ADMİN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> roleTokenContract() {
        final Function function = new Function(FUNC_ROLETOKENCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> printBalances() {
        final Function function = new Function(FUNC_PRİNTBALANCES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> assignRole(String _memberAddress, String _name,
            String _memberType) {
        final Function function = new Function(
                FUNC_ASSİGNROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_memberType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> swapRole(String _address1, String _address2) {
        final Function function = new Function(
                FUNC_SWAPROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address1), 
                new org.web3j.abi.datatypes.Address(160, _address2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeRole(String account) {
        final Function function = new Function(
                FUNC_REVOKEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getRole(String account) {
        final Function function = new Function(FUNC_GETROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Member> getMember(String account) {
        final Function function = new Function(FUNC_GETMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Member>() {}));
        return executeRemoteCallSingleValueReturn(function, Member.class);
    }

    public RemoteFunctionCall<Boolean> areSameOrGlobalMembers(String account1, String account2) {
        final Function function = new Function(FUNC_ARESAMEORGLOBALMEMBERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account1), 
                new org.web3j.abi.datatypes.Address(160, account2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<List> getLocalResourceTable(String memberType) {
        final Function function = new Function(FUNC_GETLOCALRESOURCETABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(memberType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Member>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getGlobalResourceTable() {
        final Function function = new Function(FUNC_GETGLOBALRESOURCETABLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Member>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getPrimaryHeadAddress(String regularMember) {
        final Function function = new Function(FUNC_GETPRİMARYHEADADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, regularMember)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateMemberStatus(String _memberAddress,
            String _newStatus) {
        final Function function = new Function(
                FUNC_UPDATEMEMBERSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress), 
                new org.web3j.abi.datatypes.Utf8String(_newStatus)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> penalizeMember(String _memberAddress,
            BigInteger _amount) {
        final Function function = new Function(
                FUNC_PENALİZEMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardMember(String _memberAddress,
            BigInteger _amount) {
        final Function function = new Function(
                FUNC_REWARDMEMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _memberAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static RoleBasedAccessControl load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RoleBasedAccessControl(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RoleBasedAccessControl load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RoleBasedAccessControl(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RoleBasedAccessControl load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RoleBasedAccessControl(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RoleBasedAccessControl load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RoleBasedAccessControl(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RoleBasedAccessControl> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _roleTokenContract, String _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _roleTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _admin)));
        return deployRemoteCall(RoleBasedAccessControl.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<RoleBasedAccessControl> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _roleTokenContract, String _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _roleTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _admin)));
        return deployRemoteCall(RoleBasedAccessControl.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RoleBasedAccessControl> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _roleTokenContract, String _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _roleTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _admin)));
        return deployRemoteCall(RoleBasedAccessControl.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RoleBasedAccessControl> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _roleTokenContract, String _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _roleTokenContract), 
                new org.web3j.abi.datatypes.Address(160, _admin)));
        return deployRemoteCall(RoleBasedAccessControl.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class Member extends DynamicStruct {
        public String memberAddress;

        public String name;

        public String memberType;

        public String status;

        public BigInteger lastStatusUpdate;

        public BigInteger role;

        public Member(String memberAddress, String name, String memberType, String status,
                BigInteger lastStatusUpdate, BigInteger role) {
            super(new org.web3j.abi.datatypes.Address(160, memberAddress), 
                    new org.web3j.abi.datatypes.Utf8String(name), 
                    new org.web3j.abi.datatypes.Utf8String(memberType), 
                    new org.web3j.abi.datatypes.Utf8String(status), 
                    new org.web3j.abi.datatypes.generated.Uint256(lastStatusUpdate), 
                    new org.web3j.abi.datatypes.generated.Uint8(role));
            this.memberAddress = memberAddress;
            this.name = name;
            this.memberType = memberType;
            this.status = status;
            this.lastStatusUpdate = lastStatusUpdate;
            this.role = role;
        }

        public Member(Address memberAddress, Utf8String name, Utf8String memberType,
                Utf8String status, Uint256 lastStatusUpdate, Uint8 role) {
            super(memberAddress, name, memberType, status, lastStatusUpdate, role);
            this.memberAddress = memberAddress.getValue();
            this.name = name.getValue();
            this.memberType = memberType.getValue();
            this.status = status.getValue();
            this.lastStatusUpdate = lastStatusUpdate.getValue();
            this.role = role.getValue();
        }
    }
}
