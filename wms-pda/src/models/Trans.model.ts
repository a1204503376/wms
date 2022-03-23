import { DateTime } from 'ionic-angular';
import { Utils } from "../services/Utils";
import { PageInfoModel } from "./PageInfo.Model";

// 移库明细实体类
export class TransDetailInfoModel {

    // 移库单明细id
    TransDeltailId: string;

    TransDeltailIdEx: string;

    // 组织名称
    OrgName: string;

    // 来源库位编码
    SourecLocCode: string;

    // 目的库位编码
    TargetLocCode: string;

    // 商品Id
    SkuId: string;

    SkuIdEx: string;

    // 商品编码
    SkuCode: string;

    // 商品名称
    SkuName: string;

    // 批次
    BatchNo: string;

    // 实绩数量
    ScanQty: number;

    // 剩余数量
    SurplusQty: number;

    // 计划数量
    PlanQty: number;

    // 基础计量单位名称
    BaseUmName: string;

    // 计量单位名称
    UmName: string;


    // 换算倍率
    ConvertQty: number;

    // 层级
    SkuLevel: number;


    // 规格
    SkuSpec: string;


    // 实绩数量
    DisplayScanQty: string;


    // 计划数量
    DisplayPlanQty: string;


    // 剩余量
    DisplaySurplusQty: string;


    // 商品属性
    SkuAttribute: string;


    // 序列号
    SerialNumber: string;


    // 管理方式
    IsAloneManage: boolean;

    // 显示计划量
    getStringPlanQty(): string {
        return "计划:" + this.DisplayPlanQty;
    }

    // 显示已报废量
    getStringScanQty() {
        return "已移库:" + this.DisplayScanQty;
    }

    // 货位
    getStringSourecLocCode() {
        return "从:" + this.SourecLocCode;
    }

    // 货位
    getStringTargetLocCode() {
        return "至:" + this.TargetLocCode;
    }

    // 批次
    getStringBatchNo() {
        let str = "";
        if (!Utils.isEmpty(this.BatchNo))
            str = "批次:" + this.BatchNo;
        return str;
    }

    // 显示序列号
    getStringSerialNumber() {
        let str = "";
        if (!Utils.isEmpty(this.SerialNumber))
            str = "序列号:" + this.SerialNumber;
        return str;
    }
}

// 移库处理实体类
export class TransHandleModel {
    // 移库单id
    TransBillId: string;

    TransBillIdEx: string;

    // 商品编码
    SkuCode: string;

    // 原货位
    SourceLocCode: string;

    // 目标货位
    TargetLocCode: string;

    // 批次
    BatchNo: string;


    // 操作数量
    ScanQty: number;

    // 层级
    SkuLevel: number;

    // 换算倍率
    ConvertQty: number;

    // 计量单位
    UmCode: string;

    // 计量单位名称
    UmName: string;

    // 商品ID
    SkuId: string;

    SkuIdEx: string;

    // 商品名称
    SkuName: string;

    // 库房名称
    WhName: string;

    // 备注
    TransRemark: string;

    // 规格
    SkuSpec: string;

    // 商品属性
    SkuAttribute: string;

    // 序列号
    SerialNumbers: string[];

    // 单品管理
    IsAloneManage: boolean;

    // 实验室编码
    OrgCode: string;

    // 实验室名称
    OrgName: string;
}


//  库存转移主表实体类
export class TransHeaderInfoModel {

    // 库存转移主表id
    TransBillId: number;


    // 备注
    TransRemark: string;


    // 单据状态
    TransBillState: string;


    // 单据状态名称
    TransBillStateName: string;


    // 单据编码
    TransBillNo: string;


    // 单据创建人员名称
    Creator: string;


    // 库房id
    WhId: number;


    // 库房编码
    WhCode: string;


    // 库房名称
    WhName: string;


    // 转移方式名称
    TransTypeName: string;


    // 创建时间
    CreateTime: DateTime;
}



//  库存转移管理实体类
export class TransManageModel {

    // 移库单主表
    TransHeader: TransHeaderInfoModel;


    // 移库单明细
    TransDetails: TransDetailInfoModel[];
}


// 移库单管理查询实体类
export class TransQueryModel extends PageInfoModel {

    // 单据编码
    TransBillNo: string;


    // 单据状态
    TransBillState: string;


    // 转移方式
    TransType: string;


    // 库房编码
    WhCode: string;


    // 查询未完成单据
    IsNotComplete: boolean;

    // 开始时间
    DateFrom: DateTime;

    // 结束时间
    DateEnd: DateTime;

}


//  移库单下拉列表信息实体类
export class TransSelectInfo {

    // 组织管理库房Id
    WhId: number;

    // 组织管理库房编码
    WhCode: string;

    // 组织管理库房名称
    WhName: string;
}