import { Utils } from './../services/Utils';
export class CountHandleModel {
    CountBillId: string;
    CountBillIdEx: string;
    CountBillNo: string;
    CountDetailId: string;
    CountDetailIdEx: string;
    CountStock: CountStockModel[];
    FCode: string;
    FName: string;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    OrgCode: string;
    OrgName: string;
    Sybname: string;
    WhCode: string;
    WhId: string;
    WhIdEx: string;
    ZoneId: string;
    ZoneIdEx: string;
}

export class CountHeaderInfoModel {
    CountBillId: string;
    CountBillIdEx: string;
    CountBillNo: string;
    CountBillState: string;
    CountBillStateName: string;
    CountRemark: string;
    CountTag: string;
    CountTagName: string;
    CreateTime: Date;
    Creator: string;
    FCode: string;
    FName: string;
    OrgCode: string;
    OrgName: string;
    Sybname: string;
}

export class CountLocationModel {
    CountBillNo: string;
    CountDetailId: string;
    CountDetailIdEx: string;
    Creator: string;
    LatestIn: Date;
    LatestOut: Date;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    LocState: string;
    LocStateName: string;
    UpdateTime: Date;
    WhId: string;
    WhIdEx: string;
    ZoneId: string;
    ZoneIdEx: string;
    ZoneName: string;
}

export class CountLocStockModel {
    LoctionInfo: CountLocationModel;
    StockList: CountStockModel[];
}

export class CountQueryModel {
    CountBillNo: string;
    CountBillState: string;
    DateEnd: Date;
    DateFrom: Date;
    IsNotComplete: boolean;
    OrgCode: string;
}

export class CountReplayModel {
    CountBillId: string;
    CountBillIdEx: string;
    CountBillNo: string;
    CountDetailId: string;
    CountDetailIdEx: string;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    ReplayFlag: boolean;
}

export class CountStockModel {
    BatchNO: string;
    CountBatchNO: string;
    CountQty: number;
    CountRemnantValue: number;
    CountSerialNumber: string;
    DisplaySku: string;
    FCode: string;
    FName: string;
    InsideUnit: string;
    IsAloneManage: boolean;
    IsDiff: boolean;
    LocCode: string;
    LocId: string;
    LocIdEx: string;
    OrgCode: string;
    OrgName: string;
    RemnantValue: number;
    SerialNumber: string;
    SkuCode: string;
    SkuId: string;
    SkuIdEx: string;
    SkuLevel: number;
    SkuName: string;
    SourecLocCode: string;
    StockQty: number;
    SybName: string;
    UmName: string;
    Vid: Date;

    /// 显示序列号
    get StrSerialNumber(): string {
        let str = Utils.isEmpty(this.CountSerialNumber) ? this.SerialNumber : this.CountSerialNumber;
        return !Utils.isEmpty(str) && str != " " ? ("序列号:" + str) : "";
    }

    // 显示批次
    get StrBatchNO(): string {
        if (!this.IsAloneManage) {
            let str = Utils.isEmpty(this.CountBatchNO) ? this.BatchNO : this.CountBatchNO;
            return !Utils.isEmpty(str) && str != " " ? ("批次:" + str) : "";
        }
        else {
            return "";
        }

    }

    // 显示盘点量
    get StrCountQty(): string {
        let str = "盘点量:" + this.CountQty + this.UmName;
        if (!Utils.isEmpty(this.InsideUnit))
            return str + this.CountRemnantValue + this.InsideUnit;
        return str;

    }

    // 显示库存量
    get StrStockQt() {
        let str = "库存量:" + this.StockQty + this.UmName;
        if (!Utils.isEmpty(this.InsideUnit))
            return str + this.RemnantValue + this.InsideUnit;
        return str;

    }
}

// 盘点结果实体类
export class CountResultModel {
    // 盘点单主表
    CountHeader: CountHeaderInfoModel;

    /// 盘点单明细
    CountDetails: CountLocationModel[];

    /// 盘点结果
    CountResults: CountStockModel[];
}