import { PageInfoModel } from "./PageInfo.Model";
import { Utils } from "../services/Utils";

export class StockInfo {
    _occupyQty: number;
    _stockQty: number;
    BatchNo: string;
    DisplayRemnantValue: string;
    DisplaySku: string;
    DisplayStockQty: string;
    FCode: string;
    FName: string;
    InsideUnit: string;
    LatestBillId: number;
    LocCode: string;
    LocId: number;
    OccupyQty: number;
    OrgCode: string;
    OrgId: number;
    OrgName: string;
    RemnantValue: number;
    SerialNumber: string;
    SimpleString: string;
    SkuAttribute: string;
    SkuCode: string;
    SkuId: number;
    SkuLevel: number;
    SkuName: string;
    StockId: number;
    StockQty: number;
    Sybname: string;
    UnqualifiedReason: string;
    WhCode: string;
    WhId: number;
    ZoneType: string;
}
export class StockRecordModel {
    BatchNO: string;
    DisplayOccupyQty: string;
    DisplayRemnantValue: string;
    DisplaySku: string;
    DisplayStockQty: string;
    EffectiveDate: Date;
    ExpDays: number;
    LatestIn: Date;
    LatestOut: Date;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    OccupyQty: number;
    OrgCode: string;
    OrgId: string;
    OrgIdEx: string;
    OrgName: string;
    SimpleString: string;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuName: string;
    SkuSpec: string;
    StockID: string;
    StockIDEx: string;
    StockQty: number;
    TypeName: string;
    UmCode: string;
    UmName: string;
    WhCode: string;
    WhId: string;
    WhIdEx: string;
    WhName: string;
    ZnName: string;
    // 显示库存量
    disolayString() {
        if (!Utils.isEmpty(this.DisplayRemnantValue))
            return this.DisplayStockQty + " " + this.DisplayRemnantValue;
        else
            return this.DisplayStockQty;
    }
}

/// 库存查询参数实体类
export class StockQueryModel extends PageInfoModel {

    /// 工厂编码
    FCode: string;

    /// 货位编码
    LocCode: string;

    /// 货位Id
    LocId: string;

    LocIdEx: string;

    /// 实验室编码
    OrgCode: string;

    /// 序列号
    SerialNumber: string;

    /// 条码
    SkuBarCode: string;

    /// 物料Id
    SkuId: string;

    SkuIdEx: string;

    /// 事业部
    SybName: string;

    /// 库房Id
    WhId: string;

    WhIdEx: string;

    /// 查询0库存
    ZeroStock: boolean;

    /// 库区Id
    ZoneId: string;

    ZoneIdEx: string;

    /// 库区类型
    ZoneType: string;


}

export class StockSelectModel{
    stockId:string;
    wspId:string;
    skuSpec:string;
    soBillId:string;
    soBillNo:string;
    orderNo:string;
    enterpriseName:string;
    billDetailId:string;
    wellenId:string;
}