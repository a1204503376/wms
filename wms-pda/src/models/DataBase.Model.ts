import { SkuLotModel } from '../models/SkuLotModel'
export class AsnHandleBaseInfoModel {
    baseList: BaseDataModel[];
    sundriesList: SundriesInfoModel[];
}
export class BaseDataModel {
    GroupCode: string;
    IsActive: string;
    ItemDesc: string;
    ItemValue: string;
}
export class ContainerInfoModel {
    CodeMaxLength: number;
    CtCode: string;
    CtId: number;
    CtName: string;
    CtState: string;
    CtType: string;
    CtWeight: number;
    IsActive: string;
    IsMultiNew: boolean;
    NewNum: number;
    OrgGroupId: number;
    UniqueId: number;
    WhCode: string;
    WhId: number;
    WhName: string;
}
export class ContainerStateInfoModel {
    CtCode: string;
    CtId: number;
    CtsId: number;
    CtState: string;
    GrossWeight: number;
    LocCode: string;
    NetWeight: number;
    UniqueId: number;
    WhCode: string;
    WhId: number;
    WorkDetailId: number;
}
export class ReturnAttributeModel {
    SerialNumbers: string[];
    SkuAttribute: string;
}
export class SkuInfoModel {
    DataType: string;
    DisplayMergeFlag: string;
    InspectCycle: number;
    IsActive: string;
    LowerLocation: number;
    MaintainCycle: number;
    ManageMode: string;
    ManageModeName: string;
    MaxStockQty: number;
    MergeFlag: string;
    MinStockQty: number;
    OrgCode: string;
    OrgId: number;
    OrgName: string;
    PickType: number;
    SecurityQty: number;
    SkuBarcodeList: string;
    SkuCode: string;
    SkuExpDays: number;
    SkuId: number;
    SkuImageUrl: string;
    SkuName: string;
    SkuNamePy: string;
    SkuNameS: string;
    SkuRemark: string;
    SkuTypeId: number;
    TempCode: string;
    TypeCode: string;
    TypeName: string;
    UpperLocation: number;
}
export class SkuQureyModel {
    SkuCode: string;
    SkuCodeAndBarCode: string;
    SkuName: string;
    TypeId: number;
}
export class SundriesInfoModel {
    SundriesCode: string;
    SundriesId: number;
    SundriesName: string;
    SundriesType: string;
}
export class UmSkuModel {
    ConvertQty: number;
    MaxLevelFlag: string;
    SkuId: String;
    SkuIdEx: String;
    SkuLevel: number;
    SkuSpec: string;
    UmCode: string;
    UmName: string;
    UmSkuId: String;
    UmSkuIdEx: string;
}
export class WmsLabModel {
    // 工厂代码
    FCode: string
    // 组织编码
    OrgCode: string
    // 组织名称
    OrgName: string
    // 工厂名称
    FName: string
}
export class WMSLogin {
    WAREHOSE_LOGIN_ID: number;
    identity: string;
    LOGIN_PASSWORD: string;
    WHID: number;
    username: string;
}
export class ScanModel {
    skucode: string;
    skuname: string;
    qty: string;
    um: number;
    skuLotModel: SkuLotModel = new SkuLotModel();
}