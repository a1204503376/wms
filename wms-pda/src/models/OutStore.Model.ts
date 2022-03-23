import { UmSkuModel } from "./DataBase.Model";

export class PickInfoModel {
    BaseUmName: string;
    BatchNo: string;
    BillTypeCd: string;
    ConvertQty: number;
    CtCode: string;
    DetailAmount: number;
    DetailPrice: number;
    DisplayPlanNumber: string;
    DisplyPickQty: number;
    DisplyUmName: string;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    OrgId: string;
    OrgIdEx: string;
    PickPlanId: string;
    PickPlanIdEx: string;
    PickPlanQty: number;
    PickQty: number;
    PickRealQty: number;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuName: string;
    SkuSpec: string;
    SoBillId: string;
    SoBillIdEx: string;
    SoBillNo: string;
    SoDetailId: string;
    SoDetailIdEx: string;
    StockId: string;
    StockIdEx: string;
    SurplusQty: number;
    TaskId: string;
    TaskIdEx: string;
    UmName: string;
    WellenId: string;
    WellenIdEx: string;
    WhId: string;
    WhIdEx: string;
}
export class RecommendLocCodeModel {
    BatchNO: string;
    DisplayLocCode: string;
    DisplayStockQty: string;
    LocCode: string;
    OccupyQty: number;
    SkuId: string;
    SkuIdEx: string;
    StockQty: number;
}
export class SobillCompleteModel {
    Admini_1: number[];
    Admini_2: number[];
    identity: string;
    PickUser_1: number[];
    PickUser_2: number[];
    SoBillId: string;
    SoBillIdEx: string;
    UserName: string;
}
export class SoDetailModel {
    BaseUmCode: string;
    BaseUmName: string;
    BatchNo: string;
    ConvertQty: number;
    DisplayPickQty: string;
    DisplayPlanQty: string;
    DisplaySku: string;
    InsideUnit: string;
    IsAloneManage: boolean;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    ManageMode: string;
    PlanQty: number;
    PlanQtyString: string;
    ScanQty: number;
    ScanQtyString: string;
    SerialNumber: string;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuLevel: number;
    SkuName: string;
    SkuSpec: string;
    SoBillId: string;
    SoBillIdEx: string;
    SoBillNo: string;
    SoDetailId: string;
    SoDetailIdEx: string;
    SurplusQtyString: string;
    UmBarcodeList: string;
    UmCode: string;
    UmName: string;
    umSkus: UmSkuModel[];
    ViewString: string;
}

export class SoHandleModel {
    BatchNo: string;
    ConvertQty: number;
    FCode: string;
    FName: string;
    LocCode: string;
    OrgCode: string;
    OrgName: string;
    OutstockType: string;
    ScanQty: number;
    SerialNumbers: string[];
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuLevel: number;
    SkuName: string;
    SoBillId: string;
    SoBillIdEx: string;
    SoBillNo: string;
    SundryName: string;
    SybName: string;
    UmName: string;
    InsideCount:string;
    InsideUnit:string;
}
export class SoHeaderModel {
    DisplayUser: string;
    FCode: string;
    FName: string;
    LastUpdateDate: Date;
    OrgCode: string;
    OrgName: string;
    OutstockType: string;
    OutstockTypeString: string;
    PreCreateDate: Date;
    SoBillId: string;
    SoBillIdEx: string;
    SoBillNo: string;
    SoBillRemark: string;
    SoBillState: string;
    SoBillStateString: string;
    Sybname: string;
    TransportDate: Date;
    UserCode: string;
    Username: string;
}
export class SoHeaderQueryModel {
    AllOrder: boolean;
    BillNo: string;
    BillState: string;
    FCode: string;
    OrgCode: string;
    OutstockType: string;
    QueryDate: string;
    SybName: string;
}
export class SoManageModel {
    SoDetails: SoDetailModel[];
    SoHeader: SoHeaderModel;
}
export class WmsTbPersonnelModel {
    Bm: string;
    Gw: string;
    Identity: string;
    Lab: string;
    Sybname: string;
    TbId: string;
    Usergroup: string;
    Username: string;
}