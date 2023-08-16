import { NativeModules, NativeEventEmitter } from 'react-native';

const { ChainwayC71 } = NativeModules;

const evtEmitter = new NativeEventEmitter(ChainwayC71);

type initReader = () => Promise<boolean>;

type deinitReader = () => Promise<boolean>;

type isReaderInit = () => Promise<boolean>;

type readSingleTag = () => Promise<any>;

type readPower = () => Promise<any>;

type setPower = (powerVal: number) => Promise<any>;

type writeToEpc = (newStr: string) => Promise<any>;

type addListener = (cb: (args: any[]) => void) => void;

type removeListener = (cb: (args: any[]) => void) => void;

type startScan = () => Promise<any>;

type stopScan = () => Promise<any>;

type findTag = (tag: string) => Promise<any>;

type clearTagCache = () => Promise<any>;

const initReader: initReader = () => ChainwayC71.initReader();

const deinitReader: deinitReader = () => ChainwayC71.deinitReader();

const isReaderInit: isReaderInit = () => ChainwayC71.isReaderInit();

const readSingleTag: readSingleTag = () => ChainwayC71.readSingleTag();

const readPower: readPower = () => ChainwayC71.readPower();

const setPower: setPower = (powerVal: number) => ChainwayC71.setPower(powerVal);

const writeToEpc: writeToEpc = (newStr: string) =>
  ChainwayC71.writeToEpc(newStr);

const startScan: startScan = () => ChainwayC71.startScan();

const stopScan: stopScan = () => ChainwayC71.stopScan();

const startBarcodeScan: startScan = () => ChainwayC71.barcodeRead();

const stopBarcodeScan: stopScan = () => ChainwayC71.barcodeCancel();

const findTag: findTag = (tag: string) => ChainwayC71.findTag(tag);

const addTagListener: addListener = (listener) =>
  evtEmitter.addListener('UHF_TAG', listener);

const removeTagListener: removeListener = (listener) =>
  evtEmitter.removeListener('UHF_TAG', listener);

const addBarcodeListener: addListener = (listener) =>
  evtEmitter.addListener('BARCODE', listener);

const removeBarcodeListener: removeListener = (listener) =>
  evtEmitter.removeListener('BARCODE', listener);

const clearTagCache: clearTagCache = () => ChainwayC71.clearTags();

export default {
  initReader,
  deinitReader,
  isReaderInit,
  readSingleTag,
  readPower,
  setPower,
  writeToEpc,
  startScan,
  stopScan,
  findTag,
  addTagListener,
  removeTagListener,
  addBarcodeListener,
  removeBarcodeListener,
  startBarcodeScan,
  stopBarcodeScan,
  clearTagCache,
};
