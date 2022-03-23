import { UmSkuModel } from "./DataBase.Model";
import { DateTime } from "ionic-angular";


export class AsnDetailInfoModel {
    AsnBillId: string;
    AsnBillIdEx: string;
    AsnBillNo: string;
    AsnDeltailId: number;
    AsnDetailRemark: string;
    BaseUmCode: string;
    BaseUmName: string;
    BatchNo: string;
    ConvertQty: number;
    DetailAmount: number;
    DetailPrice: number;
    DisplayPlanQty: string;
    DisplayScanQty: string;
    DisplaySku: string;
    DisplaySurplusQty: string;
    InsideCount: number;
    InsideUnit: string;
    IsAloneManage: boolean;
    LocCode: string;
    LocId: string
    LocIdEx: string;
    ManageMode: string;
    PlanQty: number;
    ScanQty: number;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string
    SkuLevel: number;
    SkuName: string;
    SkuSpec: string;
    strDisplayPlanQty: string;
    strDisplayScanQty: string;
    UmCode: string;
    UmName: string;
    umSkus: UmSkuModel[];
    ViewString: string;
}
export class AsnHandleModel {
    AsnBillId: string;
    AsnBillIdEx: string;
    AsnBillNo: string;
    BoolUnqualified: boolean;
    ConvertQty: number;
    FCode: string;
    FName: string;
    InsideUnit: string;
    InstoreType: string;
    LocCode: string;
    OrgCode: string;
    OrgId: string;
    OrgIdEx: string;
    OrgName: string;
    ProduceDate: Date;
    ProductionTime :Date;
    Salvage: number;
    ScanQtyExt:number;
    ScanQty: number;
    SerialNumbers: string[];
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuLevel: number;
    SkuName: string;
    SkuSpec: string;
    SundryName: string;
    SybName: string;
    UmCode: string;
    UmName: string;
    UnqualifiedReason: string;
    IDENTITY:string;
    USERNAME:string;
}
export class AsnHeaderInfoModel {
    ArriveDate: Date;
    AsnAmount: number;
    AsnAmountPay: number;
    AsnBillId: string;
    AsnBillIdEx: string;
    AsnBillNo: string;
    AsnBillRemark: string;
    AsnBillState: string;
    AsnBillStateName: string;
    AsnRemark: string;
    Attri1: string;
    Attri2: string;
    Attri3: string;
    Attri4: string;
    Attri5: string;
    BillKey: string;
    BillTypeCd: string;
    BillTypeName: string;
    CoCode: string;
    Contact: string;
    ContractNo: string;
    Creator: string;
    displaySName: string;
    FCode: string;
    FName: string;
    InstoreType: string;
    InstoreTypeName: string;
    LastUpdateDate: Date;
    OrderNo: string;
    OrgCode: string;
    OrgGroupId: number;
    OrgId: string;
    OrgIdEx: string;
    OrgName: string;
    Phone: string;
    PreCreateDate: Date;
    Printed: number;
    PrintedTime: Date;
    SAddress: string;
    SalesMan: string;
    ShipNo: string;
    SName: string;
    SoBillNo: string;
    Sybname: string;
    SyncState: string;
    WhCode: string;
    WhId: string;
    WhIdEx: string;
    WhName: string;
}
export class AsnManageModel {
    AsnDetails: AsnDetailInfoModel[];
    AsnHeader: AsnHeaderInfoModel;
}
export class AsnQueryModel {
    AsnBillNo: string;
    AsnBillState: string;
    BillTypeCd: string;
    DateEnd: Date;
    DateFrom: Date;
    InstoreType: string;
    IsNotComplete: boolean;
    OrgCode: string;
    WhCode: string;
}
export class SelectSkuModel {
    DisplaySku: string;
    InsideCount: number;
    InsideUnit: string;
    IsActive: string;
    IsAloneManage: boolean;
    IsCheck: boolean;
    LocCode: string;
    ManageMode: string;
    MergeFlag: string;
    OrgCode: string;
    OrgName: string;
    SkuCode: string;
    SkuId: string;
    SkuIdEx:string;
    SkuName: string;
    TypeName: string;
    UmSku: UmSkuModel[];
    Vid:Date;
    ProductionTime:Date;
}
export class SerialNumberInfoModel {
    BillDetails: AsnDetailInfoModel[];
    BillInfo: AsnHeaderInfoModel;
    SkuInfo: SelectSkuModel;
}
export class WmsRetiringStockModel {
    AttachFieldStr: string;
    FCode: string;
    InsideUnit: string;
    LatestBillId: string;
    LatestBillIdEx: string;
    OrgCode: string;
    RemnantValue: number;
    RetiringStockId: number;
    SerialNumber: string;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    StockQty: number;
    Sybname: string;
    UnqualifiedReason: string;
    WhCode: string;
    WhId: string;
    WhIdEx: string;
}