# react-native-chainway-c71

React native wrapper for the chainway sdk

## Installation

```sh
npm install react-native-chainway-c71
```

## Usage

```js
import ChainwayC71 from "react-native-chainway-c71";
// ...

const BarcodeScannerScreen: React.FC = () => {
  const [idNumber, setIdNumber] = useState("")
  useEffect(() => {
    const handleBarcodeScan = (data: any) => {
      setIdNumber(data.replace(/[^a-zA-Z0-9]/g, '').substring(0, 16));
    };
    ChainwayC72Component.initReader();

    // Add a listener for barcode scanning
    ChainwayC72Component.addBarcodeListener(handleBarcodeScan);

    // Clean up the listener when the component unmounts
    return () => {
      ChainwayC72Component.removeBarcodeListener(handleBarcodeScan);
      ChainwayC72Component.deinitReader();
    };
  }, []);

  const startBarcodeScan = async () => {
    try {
      if (await ChainwayC72Component.startBarcodeScan()) {
        console.log('Barcode scanning started');
      } else {
        throw new Error('');
      }
    } catch (error) {
      console.error('Error starting barcode scanning:', error);
    }
  };

  return (
    <View>
      <Text style={{marginVertical: 20}}>
        Barcode Scanner{idNumber && `: ${idNumber}`}
      </Text>
      <Button title="Start Scan" onPress={startBarcodeScan} />
    </View>
  );
};

export default BarcodeScannerScreen;

```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
