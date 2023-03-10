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

    /// ???????????????
    get StrSerialNumber(): string {
        let str = Utils.isEmpty(this.CountSerialNumber) ? this.SerialNumber : this.CountSerialNumber;
        return !Utils.isEmpty(str) && str != " " ? ("?????????:" + str) : "";
    }

    // ????????????
    get StrBatchNO(): string {
        if (!this.IsAloneManage) {
            let str = Utils.isEmpty(this.CountBatchNO) ? this.BatchNO : this.CountBatchNO;
            return !Utils.isEmpty(str) && str != " " ? ("??????:" + str) : "";
        }
        else {
            return "";
        }

    }

    // ???????????????
    get StrCountQty(): string {
        let str = "?????????:" + this.CountQty + this.UmName;
        if (!Utils.isEmpty(this.InsideUnit))
            return str + this.CountRemnantValue + this.InsideUnit;
        return str;

    }

    // ???????????????
    get StrStockQt() {
        let str = "?????????:" + this.StockQty + this.UmName;
        if (!Utils.isEmpty(this.InsideUnit))
            return str + this.RemnantValue + this.InsideUnit;
        return str;

    }
}

// ?????????????????????
export class CountResultModel {
    // ???????????????
    CountHeader: CountHeaderInfoModel;

    /// ???????????????
    CountDetails: CountLocationModel[];

    /// ????????????
    CountResults: CountStockModel[];
}