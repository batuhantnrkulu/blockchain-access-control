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
import org.web3j.abi.datatypes.Bool;
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
import org.web3j.tuples.generated.Tuple4;
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
public class AccessControlContract extends Contract {
    public static final String BINARY = "0x6080604052603c6003556004805460ff191660011790556201518060085534801562000029575f80fd5b5060405162002643380380620026438339810160408190526200004c9162000101565b600280546001600160a01b038085166001600160a01b0319928316179092556004805488841661010002610100600160a81b031990911617905560058054928716929091169190911790556006620000a58482620002a4565b50600780546001600160a01b0319166001600160a01b0392909216919091179055506200036c92505050565b80516001600160a01b0381168114620000e8575f80fd5b919050565b634e487b7160e01b5f52604160045260245ffd5b5f805f805f60a0868803121562000116575f80fd5b6200012186620000d1565b9450602062000132818801620000d1565b60408801519095506001600160401b03808211156200014f575f80fd5b818901915089601f83011262000163575f80fd5b815181811115620001785762000178620000ed565b604051601f8201601f19908116603f01168101908382118183101715620001a357620001a3620000ed565b816040528281528c86848701011115620001bb575f80fd5b5f93505b82841015620001de5784840186015181850187015292850192620001bf565b5f868483010152809850505050505050620001fc60608701620000d1565b91506200020c60808701620000d1565b90509295509295909350565b600181811c908216806200022d57607f821691505b6020821081036200024c57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f8211156200029f575f81815260208120601f850160051c810160208610156200027a5750805b601f850160051c820191505b818110156200029b5782815560010162000286565b5050505b505050565b81516001600160401b03811115620002c057620002c0620000ed565b620002d881620002d1845462000218565b8462000252565b602080601f8311600181146200030e575f8415620002f65750858301515b5f19600386901b1c1916600185901b1785556200029b565b5f85815260208120601f198616915b828110156200033e578886015182559484019460019091019084016200031d565b50858210156200035c57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b6122c9806200037a5f395ff3fe608060405234801561000f575f80fd5b5060043610610111575f3560e01c806358dbc45d1161009e578063afe1d5ea1161006e578063afe1d5ea14610235578063bca353be14610248578063d3e8948314610250578063d6b683fc14610273578063f03d96d014610286575f80fd5b806358dbc45d146101ef57806378f47501146102075780638fda356d1461021a578063a51b713614610222575f80fd5b806322f3e2d4116100e457806322f3e2d414610192578063235fd7e21461019f57806342cde4e8146101b257806348d33ef2146101c9578063557ed1ba146101d2575f80fd5b806302d05d3f146101155780631073d7c1146101455780631648c7b6146101685780631e3b70b41461017d575b5f80fd5b600754610128906001600160a01b031681565b6040516001600160a01b0390911681526020015b60405180910390f35b610158610153366004611b04565b610299565b604051901515815260200161013c565b61017b610176366004611b64565b610c64565b005b610185610ee8565b60405161013c9190611c33565b6004546101589060ff1681565b61017b6101ad366004611c4c565b610f74565b6101bb60035481565b60405190815260200161013c565b6101bb60085481565b6101da6111d2565b6040805192835290151560208301520161013c565b6004546101289061010090046001600160a01b031681565b600554610128906001600160a01b031681565b61017b611258565b600254610128906001600160a01b031681565b61017b610243366004611c63565b6113fe565b61017b6116a3565b61026361025e366004611c4c565b6117d4565b60405161013c9493929190611cef565b600154610128906001600160a01b031681565b61017b610294366004611d39565b6119a0565b6004545f9060ff166102c65760405162461bcd60e51b81526004016102bd90611d5f565b60405180910390fd5b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa15801561030c573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906103309190611d8f565b90508042116103515760405162461bcd60e51b81526004016102bd90611da6565b60045461010090046001600160a01b031633146103a85760405162461bcd60e51b815260206004820152601560248201527410d85b1b195c881a5cc81b9bdd081cdd589a9958dd605a1b60448201526064016102bd565b5f5b5f548110156109d95784805190602001205f82815481106103cd576103cd611dee565b905f5260205f2090600402015f016040516103e89190611e3a565b6040518091039020148015610438575083805190602001205f828154811061041257610412611dee565b905f5260205f20906004020160010160405161042e9190611e3a565b6040518091039020145b156109c7576003545f828154811061045257610452611dee565b905f5260205f2090600402016003015461046c9190611ec0565b42101561055f576001546040805163626f80e760e11b815233600482015260248101919091526013604482015272546f6f206672657175656e742061636365737360681b60648201525f91829182916001600160a01b03169063c4df01ce906084015f604051808303815f875af11580156104e9573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526105109190810190611f23565b600454604051939650919450925061010090046001600160a01b0316905f805160206122748339815191529061054b90869086908690611f6f565b60405180910390a25f955050505050610c5d565b425f828154811061057257610572611dee565b905f5260205f20906004020160030181905550600460019054906101000a90046001600160a01b03166001600160a01b03167f80a06d32e8e28dc7825be47078b4a65d24e18b98abe8ddb7467fd2e993b85d0a868660405180604001604052806005815260200164616c6c6f7760d81b815250805190602001205f86815481106105fe576105fe611dee565b905f5260205f20906004020160020160405161061a9190611e3a565b6040519081900381206106309493921490611f97565b60405180910390a2604080518082019091526005815264616c6c6f7760d81b6020909101525f80547fbcec4be69db00d4d20952ae27cd2494b0e3bf401159e19fb6165a3f0849ef9409082908490811061068c5761068c611dee565b905f5260205f2090600402016002016040516106a89190611e3a565b604051809103902014905080156107785760015460405163078d274760e21b81523360048201525f9182916001600160a01b0390911690631e349d1c906024015f604051808303815f875af1158015610703573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f1916820160405261072a9190810190611fce565b91509150336001600160a01b03167f99160c89fdccb67e1961cf554a10fffb3dbbb7b2cd44a817f2aac33deb57d9d78383604051610769929190612008565b60405180910390a250506109be565b60408051808201909152600481526319591a5d60e21b6020918201528551908601207fc1430af76e820ac7237bd100fccc681bfa953ee7522bb693b2fa2a71f1beafdd1480610809575060408051808201909152600681526564656c65746560d01b6020918201528551908601207f9fcba1e1e2be1b4668ce9e58bf81b52190e4098ed5452612e77505295dfbca56145b156108c45760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce90610842903390600401612020565b5f604051808303815f875af115801561085d573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526108849190810190611f23565b925092509250336001600160a01b03165f805160206122748339815191528484846040516108b493929190611f6f565b60405180910390a25050506109be565b6040805180820190915260048152637669657760e01b6020918201528551908601207f5d4381bb4a4c75c843c4d35f8b5a6677e24c2b3b8c8ec346faaa6201d8dd576a016109be5760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce9061094090339060040161205f565b5f604051808303815f875af115801561095b573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526109829190810190611f23565b925092509250336001600160a01b03165f805160206122748339815191528484846040516109b293929190611f6f565b60405180910390a25050505b9250610c5d9050565b806109d181612095565b9150506103aa565b5060408051808201909152600481526319591a5d60e21b6020918201528351908401207fc1430af76e820ac7237bd100fccc681bfa953ee7522bb693b2fa2a71f1beafdd1480610a6b575060408051808201909152600681526564656c65746560d01b6020918201528351908401207f9fcba1e1e2be1b4668ce9e58bf81b52190e4098ed5452612e77505295dfbca56145b15610b265760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce90610aa4903390600401612020565b5f604051808303815f875af1158015610abf573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f19168201604052610ae69190810190611f23565b925092509250336001600160a01b03165f80516020612274833981519152848484604051610b1693929190611f6f565b60405180910390a2505050610c20565b6040805180820190915260048152637669657760e01b6020918201528351908401207f5d4381bb4a4c75c843c4d35f8b5a6677e24c2b3b8c8ec346faaa6201d8dd576a01610c205760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce90610ba290339060040161205f565b5f604051808303815f875af1158015610bbd573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f19168201604052610be49190810190611f23565b925092509250336001600160a01b03165f80516020612274833981519152848484604051610c1493929190611f6f565b60405180910390a25050505b7f38df3a8dddf6ef31501df5a214b30fe934ac5f4ed0cd1b5bc31f44752486c6ac8484604051610c519291906120ad565b60405180910390a15f91505b5092915050565b60045460ff16610c865760405162461bcd60e51b81526004016102bd90611d5f565b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa158015610ccc573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610cf09190611d8f565b9050804211610d115760405162461bcd60e51b81526004016102bd90611da6565b60408051808201909152601b81525f8051602061225483398151915260208201526007546001600160a01b03163314610dfc5760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce90610d7a90339088906004016120da565b5f604051808303815f875af1158015610d95573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f19168201604052610dbc9190810190611f23565b925092509250336001600160a01b03165f80516020612274833981519152848484604051610dec93929190611f6f565b60405180910390a2505050610ee1565b60408051608081018252868152602081018690529081018490525f606082018190528054600181018255908052815160049091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56301908190610e5f9082612142565b5060208201516001820190610e749082612142565b5060408201516002820190610e899082612142565b50606091909101516003909101555f54610ea5906001906121fe565b7f4a9ad51400d5892e661ecfe449652a652a5128ec2ee03cefe5ea8251080a0a90868686604051610ed893929190612211565b60405180910390a25b5050505050565b60068054610ef590611e02565b80601f0160208091040260200160405190810160405280929190818152602001828054610f2190611e02565b8015610f6c5780601f10610f4357610100808354040283529160200191610f6c565b820191905f5260205f20905b815481529060010190602001808311610f4f57829003601f168201915b505050505081565b60045460ff16610f965760405162461bcd60e51b81526004016102bd90611d5f565b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa158015610fdc573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906110009190611d8f565b90508042116110215760405162461bcd60e51b81526004016102bd90611da6565b60408051808201909152601b81525f8051602061225483398151915260208201526007546001600160a01b0316331461110c5760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce9061108a90339088906004016120da565b5f604051808303815f875af11580156110a5573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526110cc9190810190611f23565b925092509250336001600160a01b03165f805160206122748339815191528484846040516110fc93929190611f6f565b60405180910390a2505050505050565b5f5483106111545760405162461bcd60e51b8152602060048201526015602482015274141bdb1a58de48191bd95cc81b9bdd08195e1a5cdd605a1b60448201526064016102bd565b5f838154811061116657611166611dee565b5f918252602082206004909102019061117f82826119f4565b61118c600183015f6119f4565b611199600283015f6119f4565b505f60039190910181905560405184917f433ab09ab993382af577c4a70f77dcc7cf149b5b52eb40d19b869664bfb5496d91a25b505050565b6001546040516301c2084f60e31b81523360048201525f91829182916001600160a01b031690630e10427890602401602060405180830381865afa15801561121c573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906112409190611d8f565b90505f8142101561124f575060015b90939092509050565b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa15801561129e573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906112c29190611d8f565b90508042116112e35760405162461bcd60e51b81526004016102bd90611da6565b60408051808201909152601181527044656e69616c206f66205365727669636560781b60208201526007546001600160a01b031633146113c55760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce9061135390339088906004016120da565b5f604051808303815f875af115801561136e573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526113959190810190611f23565b925092509250336001600160a01b03165f80516020612274833981519152848484604051610ed893929190611f6f565b6004805460ff191660011790556040517fe09eb0ac9bad92f05e0b489e6fc1d3e349861ffd8a2548182ef06278968a9bd0905f90a15050565b60045460ff166114205760405162461bcd60e51b81526004016102bd90611d5f565b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa158015611466573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061148a9190611d8f565b90508042116114ab5760405162461bcd60e51b81526004016102bd90611da6565b60408051808201909152601b81525f8051602061225483398151915260208201526007546001600160a01b031633146115965760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce9061151490339088906004016120da565b5f604051808303815f875af115801561152f573d5f803e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526115569190810190611f23565b925092509250336001600160a01b03165f8051602061227483398151915284848460405161158693929190611f6f565b60405180910390a250505061169b565b5f5486106115de5760405162461bcd60e51b8152602060048201526015602482015274141bdb1a58de48191bd95cc81b9bdd08195e1a5cdd605a1b60448201526064016102bd565b60405180608001604052808681526020018581526020018481526020015f8152505f878154811061161157611611611dee565b5f9182526020909120825160049092020190819061162f9082612142565b50602082015160018201906116449082612142565b50604082015160028201906116599082612142565b5060608201518160030155905050857fa39e8f99ceabdf2abb963f17fc87e85b4a88100af3822b2502ecb3655ba884198686866040516110fc93929190612211565b505050505050565b6001546040516301c2084f60e31b81523360048201525f916001600160a01b031690630e10427890602401602060405180830381865afa1580156116e9573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061170d9190611d8f565b905080421161172e5760405162461bcd60e51b81526004016102bd90611da6565b60408051808201909152601181527044656e69616c206f66205365727669636560781b60208201526007546001600160a01b0316331461179e5760015460405163626f80e760e11b81525f91829182916001600160a01b03169063c4df01ce9061135390339088906004016120da565b6004805460ff191690556040517f8c8771d60e1ae6b90556d35dc2f79a3190d6b4088c05d7ca8cabbb9e9061f3e5905f90a15050565b5f81815481106117e2575f80fd5b905f5260205f2090600402015f91509050805f01805461180190611e02565b80601f016020809104026020016040519081016040528092919081815260200182805461182d90611e02565b80156118785780601f1061184f57610100808354040283529160200191611878565b820191905f5260205f20905b81548152906001019060200180831161185b57829003601f168201915b50505050509080600101805461188d90611e02565b80601f01602080910402602001604051908101604052809291908181526020018280546118b990611e02565b80156119045780601f106118db57610100808354040283529160200191611904565b820191905f5260205f20905b8154815290600101906020018083116118e757829003601f168201915b50505050509080600201805461191990611e02565b80601f016020809104026020016040519081016040528092919081815260200182805461194590611e02565b80156119905780601f1061196757610100808354040283529160200191611990565b820191905f5260205f20905b81548152906001019060200180831161197357829003601f168201915b5050505050908060030154905084565b600180546001600160a01b0319166001600160a01b0383169081179091556040519081527f60aef7237739f69cdef5dd8e8f90780ef1f3f6d76024d66e168102d04e88b5f49060200160405180910390a150565b508054611a0090611e02565b5f825580601f10611a0f575050565b601f0160209004905f5260205f2090810190611a2b9190611a2e565b50565b5b80821115611a42575f8155600101611a2f565b5090565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f1916810167ffffffffffffffff81118282101715611a8357611a83611a46565b604052919050565b5f67ffffffffffffffff821115611aa457611aa4611a46565b50601f01601f191660200190565b5f82601f830112611ac1575f80fd5b8135611ad4611acf82611a8b565b611a5a565b818152846020838601011115611ae8575f80fd5b816020850160208301375f918101602001919091529392505050565b5f8060408385031215611b15575f80fd5b823567ffffffffffffffff80821115611b2c575f80fd5b611b3886838701611ab2565b93506020850135915080821115611b4d575f80fd5b50611b5a85828601611ab2565b9150509250929050565b5f805f60608486031215611b76575f80fd5b833567ffffffffffffffff80821115611b8d575f80fd5b611b9987838801611ab2565b94506020860135915080821115611bae575f80fd5b611bba87838801611ab2565b93506040860135915080821115611bcf575f80fd5b50611bdc86828701611ab2565b9150509250925092565b5f5b83811015611c00578181015183820152602001611be8565b50505f910152565b5f8151808452611c1f816020860160208601611be6565b601f01601f19169290920160200192915050565b602081525f611c456020830184611c08565b9392505050565b5f60208284031215611c5c575f80fd5b5035919050565b5f805f8060808587031215611c76575f80fd5b84359350602085013567ffffffffffffffff80821115611c94575f80fd5b611ca088838901611ab2565b94506040870135915080821115611cb5575f80fd5b611cc188838901611ab2565b93506060870135915080821115611cd6575f80fd5b50611ce387828801611ab2565b91505092959194509250565b608081525f611d016080830187611c08565b8281036020840152611d138187611c08565b90508281036040840152611d278186611c08565b91505082606083015295945050505050565b5f60208284031215611d49575f80fd5b81356001600160a01b0381168114611c45575f80fd5b602080825260169082015275436f6e7472616374206973206e6f742061637469766560501b604082015260600190565b5f60208284031215611d9f575f80fd5b5051919050565b60208082526028908201527f416363657373436f6e74726f6c436f6e74726163743a204d656d62657220697360408201526708189b1bd8dad95960c21b606082015260800190565b634e487b7160e01b5f52603260045260245ffd5b600181811c90821680611e1657607f821691505b602082108103611e3457634e487b7160e01b5f52602260045260245ffd5b50919050565b5f808354611e4781611e02565b60018281168015611e5f5760018114611e7457611ea0565b60ff1984168752821515830287019450611ea0565b875f526020805f205f5b85811015611e975781548a820152908401908201611e7e565b50505082870194505b50929695505050505050565b634e487b7160e01b5f52601160045260245ffd5b80820180821115611ed357611ed3611eac565b92915050565b5f82601f830112611ee8575f80fd5b8151611ef6611acf82611a8b565b818152846020838601011115611f0a575f80fd5b611f1b826020830160208701611be6565b949350505050565b5f805f60608486031215611f35575f80fd5b83519250602084015167ffffffffffffffff811115611f52575f80fd5b611f5e86828701611ed9565b925050604084015190509250925092565b838152606060208201525f611f876060830185611c08565b9050826040830152949350505050565b606081525f611fa96060830186611c08565b8281036020840152611fbb8186611c08565b9150508215156040830152949350505050565b5f8060408385031215611fdf575f80fd5b82519150602083015167ffffffffffffffff811115611ffc575f80fd5b611b5a85828601611ed9565b828152604060208201525f611f1b6040830184611c08565b6001600160a01b039190911681526040602082018190526013908201527254616d706572696e672077697468206461746160681b606082015260800190565b6001600160a01b03919091168152604060208201819052601b908201525f80516020612254833981519152606082015260800190565b5f600182016120a6576120a6611eac565b5060010190565b604081525f6120bf6040830185611c08565b82810360208401526120d18185611c08565b95945050505050565b6001600160a01b03831681526040602082018190525f90611f1b90830184611c08565b601f8211156111cd575f81815260208120601f850160051c810160208610156121235750805b601f850160051c820191505b8181101561169b5782815560010161212f565b815167ffffffffffffffff81111561215c5761215c611a46565b6121708161216a8454611e02565b846120fd565b602080601f8311600181146121a3575f841561218c5750858301515b5f19600386901b1c1916600185901b17855561169b565b5f85815260208120601f198616915b828110156121d1578886015182559484019460019091019084016121b2565b50858210156121ee57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b81810381811115611ed357611ed3611eac565b606081525f6122236060830186611c08565b82810360208401526122358186611c08565b905082810360408401526122498185611c08565b969550505050505056fe556e617574686f72697a65642061636365737320617474656d707400000000003fc8de32d0ff2767d320c8c52fd3ab1a29ddf9484b23575845d542369bdfa37fa26469706673582212205ac2deda54f215d23314dce8da3f30fefc0d43a9a949617e5c1367c6295177fe64736f6c63430008150033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ACCTYPE = "accType";

    public static final String FUNC_BENİGNTHRESHOLD = "benignThreshold";

    public static final String FUNC_CREATOR = "creator";

    public static final String FUNC_İSACTİVE = "isActive";

    public static final String FUNC_JUDGECONTRACT = "judgeContract";

    public static final String FUNC_OBJECTADDRESS = "objectAddress";

    public static final String FUNC_POLİCİES = "policies";

    public static final String FUNC_ROLEBASEDCONTRACT = "roleBasedContract";

    public static final String FUNC_SUBJECTADDRESS = "subjectAddress";

    public static final String FUNC_THRESHOLD = "threshold";

    public static final String FUNC_SETJUDGECONTRACT = "setJudgeContract";

    public static final String FUNC_POLİCYADD = "policyAdd";

    public static final String FUNC_POLİCYUPDATE = "policyUpdate";

    public static final String FUNC_POLİCYDELETE = "policyDelete";

    public static final String FUNC_GETTİME = "getTime";

    public static final String FUNC_ACCESSCONTROL = "accessControl";

    public static final String FUNC_ACTİVATECONTRACT = "activateContract";

    public static final String FUNC_DEACTİVATECONTRACT = "deactivateContract";

    public static final Event ACCESSCONTROLCHECKED_EVENT = new Event("AccessControlChecked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event CONTRACTACTİVATED_EVENT = new Event("ContractActivated", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event CONTRACTDEACTİVATED_EVENT = new Event("ContractDeactivated", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event DATATRANSFERRED_EVENT = new Event("DataTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event FUNCTİONCALLED_EVENT = new Event("FunctionCalled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event JUDGECONTRACTSET_EVENT = new Event("JudgeContractSet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event MALİCİOUSACTİVİTYREPORTED_EVENT = new Event("MaliciousActivityReported", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event NONPENALİZEMİSBEHAVİORREPORTED_EVENT = new Event("NonPenalizeMisbehaviorReported", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event POLİCYADDED_EVENT = new Event("PolicyAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event POLİCYDELETED_EVENT = new Event("PolicyDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final Event POLİCYNOTFOUNDFORACCESSCONTROL_EVENT = new Event("PolicyNotFoundForAccessControl", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event POLİCYUPDATED_EVENT = new Event("PolicyUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ROLEBASEDCONTRACTSET_EVENT = new Event("RoleBasedContractSet", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected AccessControlContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AccessControlContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AccessControlContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AccessControlContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AccessControlCheckedEventResponse> getAccessControlCheckedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSCONTROLCHECKED_EVENT, transactionReceipt);
        ArrayList<AccessControlCheckedEventResponse> responses = new ArrayList<AccessControlCheckedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessControlCheckedEventResponse typedResponse = new AccessControlCheckedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.allowed = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessControlCheckedEventResponse getAccessControlCheckedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSCONTROLCHECKED_EVENT, log);
        AccessControlCheckedEventResponse typedResponse = new AccessControlCheckedEventResponse();
        typedResponse.log = log;
        typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.allowed = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<AccessControlCheckedEventResponse> accessControlCheckedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessControlCheckedEventFromLog(log));
    }

    public Flowable<AccessControlCheckedEventResponse> accessControlCheckedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSCONTROLCHECKED_EVENT));
        return accessControlCheckedEventFlowable(filter);
    }

    public static List<ContractActivatedEventResponse> getContractActivatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CONTRACTACTİVATED_EVENT, transactionReceipt);
        ArrayList<ContractActivatedEventResponse> responses = new ArrayList<ContractActivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContractActivatedEventResponse typedResponse = new ContractActivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ContractActivatedEventResponse getContractActivatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CONTRACTACTİVATED_EVENT, log);
        ContractActivatedEventResponse typedResponse = new ContractActivatedEventResponse();
        typedResponse.log = log;
        return typedResponse;
    }

    public Flowable<ContractActivatedEventResponse> contractActivatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getContractActivatedEventFromLog(log));
    }

    public Flowable<ContractActivatedEventResponse> contractActivatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTACTİVATED_EVENT));
        return contractActivatedEventFlowable(filter);
    }

    public static List<ContractDeactivatedEventResponse> getContractDeactivatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CONTRACTDEACTİVATED_EVENT, transactionReceipt);
        ArrayList<ContractDeactivatedEventResponse> responses = new ArrayList<ContractDeactivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContractDeactivatedEventResponse typedResponse = new ContractDeactivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ContractDeactivatedEventResponse getContractDeactivatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CONTRACTDEACTİVATED_EVENT, log);
        ContractDeactivatedEventResponse typedResponse = new ContractDeactivatedEventResponse();
        typedResponse.log = log;
        return typedResponse;
    }

    public Flowable<ContractDeactivatedEventResponse> contractDeactivatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getContractDeactivatedEventFromLog(log));
    }

    public Flowable<ContractDeactivatedEventResponse> contractDeactivatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTDEACTİVATED_EVENT));
        return contractDeactivatedEventFlowable(filter);
    }

    public static List<DataTransferredEventResponse> getDataTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DATATRANSFERRED_EVENT, transactionReceipt);
        ArrayList<DataTransferredEventResponse> responses = new ArrayList<DataTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DataTransferredEventResponse typedResponse = new DataTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.data = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DataTransferredEventResponse getDataTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DATATRANSFERRED_EVENT, log);
        DataTransferredEventResponse typedResponse = new DataTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.data = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<DataTransferredEventResponse> dataTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDataTransferredEventFromLog(log));
    }

    public Flowable<DataTransferredEventResponse> dataTransferredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DATATRANSFERRED_EVENT));
        return dataTransferredEventFlowable(filter);
    }

    public static List<FunctionCalledEventResponse> getFunctionCalledEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FUNCTİONCALLED_EVENT, transactionReceipt);
        ArrayList<FunctionCalledEventResponse> responses = new ArrayList<FunctionCalledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FunctionCalledEventResponse typedResponse = new FunctionCalledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.functionName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FunctionCalledEventResponse getFunctionCalledEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FUNCTİONCALLED_EVENT, log);
        FunctionCalledEventResponse typedResponse = new FunctionCalledEventResponse();
        typedResponse.log = log;
        typedResponse.functionName = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<FunctionCalledEventResponse> functionCalledEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFunctionCalledEventFromLog(log));
    }

    public Flowable<FunctionCalledEventResponse> functionCalledEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FUNCTİONCALLED_EVENT));
        return functionCalledEventFlowable(filter);
    }

    public static List<JudgeContractSetEventResponse> getJudgeContractSetEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(JUDGECONTRACTSET_EVENT, transactionReceipt);
        ArrayList<JudgeContractSetEventResponse> responses = new ArrayList<JudgeContractSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            JudgeContractSetEventResponse typedResponse = new JudgeContractSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.judgeContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static JudgeContractSetEventResponse getJudgeContractSetEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(JUDGECONTRACTSET_EVENT, log);
        JudgeContractSetEventResponse typedResponse = new JudgeContractSetEventResponse();
        typedResponse.log = log;
        typedResponse.judgeContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<JudgeContractSetEventResponse> judgeContractSetEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getJudgeContractSetEventFromLog(log));
    }

    public Flowable<JudgeContractSetEventResponse> judgeContractSetEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(JUDGECONTRACTSET_EVENT));
        return judgeContractSetEventFlowable(filter);
    }

    public static List<MaliciousActivityReportedEventResponse> getMaliciousActivityReportedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MALİCİOUSACTİVİTYREPORTED_EVENT, transactionReceipt);
        ArrayList<MaliciousActivityReportedEventResponse> responses = new ArrayList<MaliciousActivityReportedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MaliciousActivityReportedEventResponse typedResponse = new MaliciousActivityReportedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.penaltyAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.blockingEndTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MaliciousActivityReportedEventResponse getMaliciousActivityReportedEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MALİCİOUSACTİVİTYREPORTED_EVENT, log);
        MaliciousActivityReportedEventResponse typedResponse = new MaliciousActivityReportedEventResponse();
        typedResponse.log = log;
        typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.penaltyAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.reason = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.blockingEndTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<MaliciousActivityReportedEventResponse> maliciousActivityReportedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMaliciousActivityReportedEventFromLog(log));
    }

    public Flowable<MaliciousActivityReportedEventResponse> maliciousActivityReportedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MALİCİOUSACTİVİTYREPORTED_EVENT));
        return maliciousActivityReportedEventFlowable(filter);
    }

    public static List<NonPenalizeMisbehaviorReportedEventResponse> getNonPenalizeMisbehaviorReportedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NONPENALİZEMİSBEHAVİORREPORTED_EVENT, transactionReceipt);
        ArrayList<NonPenalizeMisbehaviorReportedEventResponse> responses = new ArrayList<NonPenalizeMisbehaviorReportedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NonPenalizeMisbehaviorReportedEventResponse typedResponse = new NonPenalizeMisbehaviorReportedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.rewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newStatus = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NonPenalizeMisbehaviorReportedEventResponse getNonPenalizeMisbehaviorReportedEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NONPENALİZEMİSBEHAVİORREPORTED_EVENT, log);
        NonPenalizeMisbehaviorReportedEventResponse typedResponse = new NonPenalizeMisbehaviorReportedEventResponse();
        typedResponse.log = log;
        typedResponse.subject = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.rewardAmount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newStatus = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<NonPenalizeMisbehaviorReportedEventResponse> nonPenalizeMisbehaviorReportedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNonPenalizeMisbehaviorReportedEventFromLog(log));
    }

    public Flowable<NonPenalizeMisbehaviorReportedEventResponse> nonPenalizeMisbehaviorReportedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NONPENALİZEMİSBEHAVİORREPORTED_EVENT));
        return nonPenalizeMisbehaviorReportedEventFlowable(filter);
    }

    public static List<PolicyAddedEventResponse> getPolicyAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(POLİCYADDED_EVENT, transactionReceipt);
        ArrayList<PolicyAddedEventResponse> responses = new ArrayList<PolicyAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PolicyAddedEventResponse typedResponse = new PolicyAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.permission = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PolicyAddedEventResponse getPolicyAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(POLİCYADDED_EVENT, log);
        PolicyAddedEventResponse typedResponse = new PolicyAddedEventResponse();
        typedResponse.log = log;
        typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.permission = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<PolicyAddedEventResponse> policyAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPolicyAddedEventFromLog(log));
    }

    public Flowable<PolicyAddedEventResponse> policyAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POLİCYADDED_EVENT));
        return policyAddedEventFlowable(filter);
    }

    public static List<PolicyDeletedEventResponse> getPolicyDeletedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(POLİCYDELETED_EVENT, transactionReceipt);
        ArrayList<PolicyDeletedEventResponse> responses = new ArrayList<PolicyDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PolicyDeletedEventResponse typedResponse = new PolicyDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PolicyDeletedEventResponse getPolicyDeletedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(POLİCYDELETED_EVENT, log);
        PolicyDeletedEventResponse typedResponse = new PolicyDeletedEventResponse();
        typedResponse.log = log;
        typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<PolicyDeletedEventResponse> policyDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPolicyDeletedEventFromLog(log));
    }

    public Flowable<PolicyDeletedEventResponse> policyDeletedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POLİCYDELETED_EVENT));
        return policyDeletedEventFlowable(filter);
    }

    public static List<PolicyNotFoundForAccessControlEventResponse> getPolicyNotFoundForAccessControlEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(POLİCYNOTFOUNDFORACCESSCONTROL_EVENT, transactionReceipt);
        ArrayList<PolicyNotFoundForAccessControlEventResponse> responses = new ArrayList<PolicyNotFoundForAccessControlEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PolicyNotFoundForAccessControlEventResponse typedResponse = new PolicyNotFoundForAccessControlEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PolicyNotFoundForAccessControlEventResponse getPolicyNotFoundForAccessControlEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(POLİCYNOTFOUNDFORACCESSCONTROL_EVENT, log);
        PolicyNotFoundForAccessControlEventResponse typedResponse = new PolicyNotFoundForAccessControlEventResponse();
        typedResponse.log = log;
        typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<PolicyNotFoundForAccessControlEventResponse> policyNotFoundForAccessControlEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPolicyNotFoundForAccessControlEventFromLog(log));
    }

    public Flowable<PolicyNotFoundForAccessControlEventResponse> policyNotFoundForAccessControlEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POLİCYNOTFOUNDFORACCESSCONTROL_EVENT));
        return policyNotFoundForAccessControlEventFlowable(filter);
    }

    public static List<PolicyUpdatedEventResponse> getPolicyUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(POLİCYUPDATED_EVENT, transactionReceipt);
        ArrayList<PolicyUpdatedEventResponse> responses = new ArrayList<PolicyUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PolicyUpdatedEventResponse typedResponse = new PolicyUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.permission = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PolicyUpdatedEventResponse getPolicyUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(POLİCYUPDATED_EVENT, log);
        PolicyUpdatedEventResponse typedResponse = new PolicyUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.policyId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.resource = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.permission = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<PolicyUpdatedEventResponse> policyUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPolicyUpdatedEventFromLog(log));
    }

    public Flowable<PolicyUpdatedEventResponse> policyUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POLİCYUPDATED_EVENT));
        return policyUpdatedEventFlowable(filter);
    }

    public static List<RoleBasedContractSetEventResponse> getRoleBasedContractSetEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEBASEDCONTRACTSET_EVENT, transactionReceipt);
        ArrayList<RoleBasedContractSetEventResponse> responses = new ArrayList<RoleBasedContractSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleBasedContractSetEventResponse typedResponse = new RoleBasedContractSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roleBasedContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RoleBasedContractSetEventResponse getRoleBasedContractSetEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ROLEBASEDCONTRACTSET_EVENT, log);
        RoleBasedContractSetEventResponse typedResponse = new RoleBasedContractSetEventResponse();
        typedResponse.log = log;
        typedResponse.roleBasedContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<RoleBasedContractSetEventResponse> roleBasedContractSetEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRoleBasedContractSetEventFromLog(log));
    }

    public Flowable<RoleBasedContractSetEventResponse> roleBasedContractSetEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEBASEDCONTRACTSET_EVENT));
        return roleBasedContractSetEventFlowable(filter);
    }

    public RemoteFunctionCall<String> accType() {
        final Function function = new Function(FUNC_ACCTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> benignThreshold() {
        final Function function = new Function(FUNC_BENİGNTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> creator() {
        final Function function = new Function(FUNC_CREATOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isActive() {
        final Function function = new Function(FUNC_İSACTİVE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> judgeContract() {
        final Function function = new Function(FUNC_JUDGECONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> objectAddress() {
        final Function function = new Function(FUNC_OBJECTADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple4<String, String, String, BigInteger>> policies(
            BigInteger param0) {
        final Function function = new Function(FUNC_POLİCİES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, String, BigInteger>>(function,
                new Callable<Tuple4<String, String, String, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> roleBasedContract() {
        final Function function = new Function(FUNC_ROLEBASEDCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> subjectAddress() {
        final Function function = new Function(FUNC_SUBJECTADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> threshold() {
        final Function function = new Function(FUNC_THRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setJudgeContract(String _judgeContract) {
        final Function function = new Function(
                FUNC_SETJUDGECONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _judgeContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> policyAdd(String resource, String action,
            String permission) {
        final Function function = new Function(
                FUNC_POLİCYADD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(resource), 
                new org.web3j.abi.datatypes.Utf8String(action), 
                new org.web3j.abi.datatypes.Utf8String(permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> policyUpdate(BigInteger policyId, String resource,
            String action, String permission) {
        final Function function = new Function(
                FUNC_POLİCYUPDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(policyId), 
                new org.web3j.abi.datatypes.Utf8String(resource), 
                new org.web3j.abi.datatypes.Utf8String(action), 
                new org.web3j.abi.datatypes.Utf8String(permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> policyDelete(BigInteger policyId) {
        final Function function = new Function(
                FUNC_POLİCYDELETE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(policyId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, Boolean>> getTime() {
        final Function function = new Function(FUNC_GETTİME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, Boolean>>(function,
                new Callable<Tuple2<BigInteger, Boolean>>() {
                    @Override
                    public Tuple2<BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> accessControl(String resource, String action) {
        final Function function = new Function(
                FUNC_ACCESSCONTROL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(resource), 
                new org.web3j.abi.datatypes.Utf8String(action)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> activateContract() {
        final Function function = new Function(
                FUNC_ACTİVATECONTRACT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deactivateContract() {
        final Function function = new Function(
                FUNC_DEACTİVATECONTRACT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AccessControlContract load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccessControlContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AccessControlContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccessControlContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AccessControlContract load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AccessControlContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AccessControlContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AccessControlContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AccessControlContract> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _subjectAddress, String _objectAddress,
            String _accType, String _roleBasedContract, String _creator) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _subjectAddress), 
                new org.web3j.abi.datatypes.Address(160, _objectAddress), 
                new org.web3j.abi.datatypes.Utf8String(_accType), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedContract), 
                new org.web3j.abi.datatypes.Address(160, _creator)));
        return deployRemoteCall(AccessControlContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<AccessControlContract> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _subjectAddress, String _objectAddress, String _accType,
            String _roleBasedContract, String _creator) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _subjectAddress), 
                new org.web3j.abi.datatypes.Address(160, _objectAddress), 
                new org.web3j.abi.datatypes.Utf8String(_accType), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedContract), 
                new org.web3j.abi.datatypes.Address(160, _creator)));
        return deployRemoteCall(AccessControlContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AccessControlContract> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _subjectAddress, String _objectAddress,
            String _accType, String _roleBasedContract, String _creator) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _subjectAddress), 
                new org.web3j.abi.datatypes.Address(160, _objectAddress), 
                new org.web3j.abi.datatypes.Utf8String(_accType), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedContract), 
                new org.web3j.abi.datatypes.Address(160, _creator)));
        return deployRemoteCall(AccessControlContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AccessControlContract> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _subjectAddress, String _objectAddress, String _accType,
            String _roleBasedContract, String _creator) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _subjectAddress), 
                new org.web3j.abi.datatypes.Address(160, _objectAddress), 
                new org.web3j.abi.datatypes.Utf8String(_accType), 
                new org.web3j.abi.datatypes.Address(160, _roleBasedContract), 
                new org.web3j.abi.datatypes.Address(160, _creator)));
        return deployRemoteCall(AccessControlContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class AccessControlCheckedEventResponse extends BaseEventResponse {
        public String subject;

        public String resource;

        public String action;

        public Boolean allowed;
    }

    public static class ContractActivatedEventResponse extends BaseEventResponse {
    }

    public static class ContractDeactivatedEventResponse extends BaseEventResponse {
    }

    public static class DataTransferredEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public String data;
    }

    public static class FunctionCalledEventResponse extends BaseEventResponse {
        public String functionName;
    }

    public static class JudgeContractSetEventResponse extends BaseEventResponse {
        public String judgeContract;
    }

    public static class MaliciousActivityReportedEventResponse extends BaseEventResponse {
        public String subject;

        public BigInteger penaltyAmount;

        public String reason;

        public BigInteger blockingEndTime;
    }

    public static class NonPenalizeMisbehaviorReportedEventResponse extends BaseEventResponse {
        public String subject;

        public BigInteger rewardAmount;

        public String newStatus;
    }

    public static class PolicyAddedEventResponse extends BaseEventResponse {
        public BigInteger policyId;

        public String resource;

        public String action;

        public String permission;
    }

    public static class PolicyDeletedEventResponse extends BaseEventResponse {
        public BigInteger policyId;
    }

    public static class PolicyNotFoundForAccessControlEventResponse extends BaseEventResponse {
        public String resource;

        public String action;
    }

    public static class PolicyUpdatedEventResponse extends BaseEventResponse {
        public BigInteger policyId;

        public String resource;

        public String action;

        public String permission;
    }

    public static class RoleBasedContractSetEventResponse extends BaseEventResponse {
        public String roleBasedContract;
    }
}
