import { NativeModules, NativeEventEmitter } from 'react-native';

const { ChainwayC71 } = NativeModules;

const evtEmitter = new NativeEventEmitter(ChainwayC71);

type initReader = () => Promise<boolean>;

type deinitReader = () => Promise<boolean>;

type addListener = (cb: (args: any[]) => void) => void;

type removeListener = (cb: (args: any[]) => void) => void;

type startScan = () => Promise<any>;

type stopScan = () => Promise<any>;

type isReaderInit = () => Promise<any>;

const initReader: initReader = () => ChainwayC71.initReader();

const deinitReader: deinitReader = () => ChainwayC71.deinitReader();

const isReaderInit: isReaderInit = () => ChainwayC71.isReaderInit();

const startBarcodeScan: startScan = () => ChainwayC71.barcodeRead();

const stopBarcodeScan: stopScan = () => ChainwayC71.barcodeCancel();

const addBarcodeListener: addListener = (listener) =>
  evtEmitter.addListener('BARCODE', listener);

const removeBarcodeListener: removeListener = (_listener) =>
  // evtEmitter.removeListener('BARCODE', listener);
  evtEmitter.removeAllListeners('BARCODE');

export default {
  initReader,
  deinitReader,
  isReaderInit,
  addBarcodeListener,
  removeBarcodeListener,
  startBarcodeScan,
  stopBarcodeScan,
};
