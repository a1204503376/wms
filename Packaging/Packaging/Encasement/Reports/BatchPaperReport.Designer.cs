namespace Packaging.Encasement.Reports
{
    partial class BatchPaperReport
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            DevExpress.XtraPrinting.BarCode.QRCodeGenerator qrCodeGenerator1 = new DevExpress.XtraPrinting.BarCode.QRCodeGenerator();
            this.TopMargin = new DevExpress.XtraReports.UI.TopMarginBand();
            this.BottomMargin = new DevExpress.XtraReports.UI.BottomMarginBand();
            this.Detail = new DevExpress.XtraReports.UI.DetailBand();
            this.xrLabel7 = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel6 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbQty4 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbQty3 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbQty2 = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel10 = new DevExpress.XtraReports.UI.XRLabel();
            this.bcBoxNumber = new DevExpress.XtraReports.UI.XRBarCode();
            this.lbSkuName = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel2 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbModel = new DevExpress.XtraReports.UI.XRLabel();
            this.lbDate = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel1 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbUserName = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel3 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbQty1 = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel4 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbBoxNumber = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel5 = new DevExpress.XtraReports.UI.XRLabel();
            this.objectDataSource1 = new DevExpress.DataAccess.ObjectBinding.ObjectDataSource(this.components);
            this.xrControlStyle1 = new DevExpress.XtraReports.UI.XRControlStyle();
            ((System.ComponentModel.ISupportInitialize)(this.objectDataSource1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this)).BeginInit();
            // 
            // TopMargin
            // 
            this.TopMargin.Dpi = 254F;
            this.TopMargin.HeightF = 0F;
            this.TopMargin.Name = "TopMargin";
            // 
            // BottomMargin
            // 
            this.BottomMargin.Dpi = 254F;
            this.BottomMargin.Font = new System.Drawing.Font("微软雅黑", 10.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.BottomMargin.HeightF = 0F;
            this.BottomMargin.Name = "BottomMargin";
            this.BottomMargin.StylePriority.UseFont = false;
            // 
            // Detail
            // 
            this.Detail.Controls.AddRange(new DevExpress.XtraReports.UI.XRControl[] {
            this.xrLabel7,
            this.xrLabel6,
            this.lbQty4,
            this.lbQty3,
            this.lbQty2,
            this.xrLabel10,
            this.bcBoxNumber,
            this.lbSkuName,
            this.xrLabel2,
            this.lbModel,
            this.lbDate,
            this.xrLabel1,
            this.lbUserName,
            this.xrLabel3,
            this.lbQty1,
            this.xrLabel4,
            this.lbBoxNumber,
            this.xrLabel5});
            this.Detail.Dpi = 254F;
            this.Detail.HeightF = 797.9833F;
            this.Detail.HierarchyPrintOptions.Indent = 50.8F;
            this.Detail.Name = "Detail";
            // 
            // xrLabel7
            // 
            this.xrLabel7.AutoWidth = true;
            this.xrLabel7.Dpi = 254F;
            this.xrLabel7.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[BoxNumber]")});
            this.xrLabel7.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel7.LocationFloat = new DevExpress.Utils.PointFloat(287.8351F, 695.9501F);
            this.xrLabel7.Multiline = true;
            this.xrLabel7.Name = "xrLabel7";
            this.xrLabel7.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel7.SizeF = new System.Drawing.SizeF(350.7434F, 57F);
            this.xrLabel7.StyleName = "xrControlStyle1";
            this.xrLabel7.TextAlignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            // 
            // xrLabel6
            // 
            this.xrLabel6.Dpi = 254F;
            this.xrLabel6.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Udf2]")});
            this.xrLabel6.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel6.LocationFloat = new DevExpress.Utils.PointFloat(678.7113F, 590.2668F);
            this.xrLabel6.Multiline = true;
            this.xrLabel6.Name = "xrLabel6";
            this.xrLabel6.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel6.SizeF = new System.Drawing.SizeF(301.4753F, 57F);
            this.xrLabel6.StyleName = "xrControlStyle1";
            // 
            // lbQty4
            // 
            this.lbQty4.Dpi = 254F;
            this.lbQty4.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Qty4]")});
            this.lbQty4.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbQty4.LocationFloat = new DevExpress.Utils.PointFloat(558.4078F, 365.6017F);
            this.lbQty4.Multiline = true;
            this.lbQty4.Name = "lbQty4";
            this.lbQty4.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbQty4.SizeF = new System.Drawing.SizeF(408.6799F, 57F);
            this.lbQty4.StyleName = "xrControlStyle1";
            // 
            // lbQty3
            // 
            this.lbQty3.Dpi = 254F;
            this.lbQty3.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Qty3]")});
            this.lbQty3.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbQty3.LocationFloat = new DevExpress.Utils.PointFloat(142.1252F, 365.6017F);
            this.lbQty3.Multiline = true;
            this.lbQty3.Name = "lbQty3";
            this.lbQty3.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbQty3.SizeF = new System.Drawing.SizeF(382F, 57F);
            this.lbQty3.StyleName = "xrControlStyle1";
            // 
            // lbQty2
            // 
            this.lbQty2.Dpi = 254F;
            this.lbQty2.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Qty2]")});
            this.lbQty2.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbQty2.LocationFloat = new DevExpress.Utils.PointFloat(558.4078F, 294.9869F);
            this.lbQty2.Multiline = true;
            this.lbQty2.Name = "lbQty2";
            this.lbQty2.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbQty2.SizeF = new System.Drawing.SizeF(408.6799F, 57F);
            this.lbQty2.StyleName = "xrControlStyle1";
            // 
            // xrLabel10
            // 
            this.xrLabel10.Dpi = 254F;
            this.xrLabel10.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[SpecialCustomer]")});
            this.xrLabel10.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel10.LocationFloat = new DevExpress.Utils.PointFloat(678.7113F, 500.6022F);
            this.xrLabel10.Multiline = true;
            this.xrLabel10.Name = "xrLabel10";
            this.xrLabel10.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel10.SizeF = new System.Drawing.SizeF(301.4753F, 56.99994F);
            this.xrLabel10.StyleName = "xrControlStyle1";
            // 
            // bcBoxNumber
            // 
            this.bcBoxNumber.Alignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            this.bcBoxNumber.AutoModule = true;
            this.bcBoxNumber.Dpi = 254F;
            this.bcBoxNumber.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[BoxNumberLabel]")});
            this.bcBoxNumber.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.bcBoxNumber.LocationFloat = new DevExpress.Utils.PointFloat(287.5785F, 442.6544F);
            this.bcBoxNumber.Module = 10.16F;
            this.bcBoxNumber.Name = "bcBoxNumber";
            this.bcBoxNumber.Padding = new DevExpress.XtraPrinting.PaddingInfo(25, 25, 0, 0, 254F);
            this.bcBoxNumber.ShowText = false;
            this.bcBoxNumber.SizeF = new System.Drawing.SizeF(351F, 253.2958F);
            this.bcBoxNumber.StylePriority.UseFont = false;
            this.bcBoxNumber.StylePriority.UseTextAlignment = false;
            qrCodeGenerator1.CompactionMode = DevExpress.XtraPrinting.BarCode.QRCodeCompactionMode.Byte;
            qrCodeGenerator1.Version = DevExpress.XtraPrinting.BarCode.QRCodeVersion.Version1;
            this.bcBoxNumber.Symbology = qrCodeGenerator1;
            this.bcBoxNumber.TextAlignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            // 
            // lbSkuName
            // 
            this.lbSkuName.Dpi = 254F;
            this.lbSkuName.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[SkuNameS]")});
            this.lbSkuName.Font = new System.Drawing.Font("微软雅黑", 15F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbSkuName.LocationFloat = new DevExpress.Utils.PointFloat(0F, 0F);
            this.lbSkuName.Multiline = true;
            this.lbSkuName.Name = "lbSkuName";
            this.lbSkuName.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbSkuName.SizeF = new System.Drawing.SizeF(1000F, 100F);
            this.lbSkuName.StylePriority.UseFont = false;
            this.lbSkuName.StylePriority.UseTextAlignment = false;
            this.lbSkuName.Text = "物料名称";
            this.lbSkuName.TextAlignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            // 
            // xrLabel2
            // 
            this.xrLabel2.AutoWidth = true;
            this.xrLabel2.Dpi = 254F;
            this.xrLabel2.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel2.LocationFloat = new DevExpress.Utils.PointFloat(26.76362F, 120.1641F);
            this.xrLabel2.Multiline = true;
            this.xrLabel2.Name = "xrLabel2";
            this.xrLabel2.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel2.SizeF = new System.Drawing.SizeF(115F, 57F);
            this.xrLabel2.StyleName = "xrControlStyle1";
            this.xrLabel2.Text = "型号:";
            // 
            // lbModel
            // 
            this.lbModel.AutoWidth = true;
            this.lbModel.Dpi = 254F;
            this.lbModel.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Model]")});
            this.lbModel.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbModel.LocationFloat = new DevExpress.Utils.PointFloat(141.7635F, 120.1641F);
            this.lbModel.Multiline = true;
            this.lbModel.Name = "lbModel";
            this.lbModel.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbModel.SizeF = new System.Drawing.SizeF(382.3616F, 57F);
            this.lbModel.StyleName = "xrControlStyle1";
            // 
            // lbDate
            // 
            this.lbDate.Dpi = 254F;
            this.lbDate.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[PrintDate]")});
            this.lbDate.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbDate.LocationFloat = new DevExpress.Utils.PointFloat(673.4078F, 120.164F);
            this.lbDate.Multiline = true;
            this.lbDate.Name = "lbDate";
            this.lbDate.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbDate.SizeF = new System.Drawing.SizeF(293.6799F, 57.00003F);
            this.lbDate.StyleName = "xrControlStyle1";
            // 
            // xrLabel1
            // 
            this.xrLabel1.Dpi = 254F;
            this.xrLabel1.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel1.LocationFloat = new DevExpress.Utils.PointFloat(558.4078F, 120.164F);
            this.xrLabel1.Multiline = true;
            this.xrLabel1.Name = "xrLabel1";
            this.xrLabel1.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel1.SizeF = new System.Drawing.SizeF(115F, 57F);
            this.xrLabel1.StyleName = "xrControlStyle1";
            this.xrLabel1.Text = "日期:";
            // 
            // lbUserName
            // 
            this.lbUserName.Dpi = 254F;
            this.lbUserName.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[UserName]")});
            this.lbUserName.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbUserName.LocationFloat = new DevExpress.Utils.PointFloat(142.1252F, 205.1592F);
            this.lbUserName.Multiline = true;
            this.lbUserName.Name = "lbUserName";
            this.lbUserName.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbUserName.SizeF = new System.Drawing.SizeF(382F, 57F);
            this.lbUserName.StyleName = "xrControlStyle1";
            // 
            // xrLabel3
            // 
            this.xrLabel3.Dpi = 254F;
            this.xrLabel3.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel3.LocationFloat = new DevExpress.Utils.PointFloat(27.02019F, 205.1593F);
            this.xrLabel3.Multiline = true;
            this.xrLabel3.Name = "xrLabel3";
            this.xrLabel3.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel3.SizeF = new System.Drawing.SizeF(115F, 57F);
            this.xrLabel3.StyleName = "xrControlStyle1";
            this.xrLabel3.Text = "工号:";
            // 
            // lbQty1
            // 
            this.lbQty1.Dpi = 254F;
            this.lbQty1.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Qty1]")});
            this.lbQty1.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbQty1.LocationFloat = new DevExpress.Utils.PointFloat(142.1252F, 294.9869F);
            this.lbQty1.Multiline = true;
            this.lbQty1.Name = "lbQty1";
            this.lbQty1.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbQty1.SizeF = new System.Drawing.SizeF(382F, 56.99997F);
            this.lbQty1.StyleName = "xrControlStyle1";
            // 
            // xrLabel4
            // 
            this.xrLabel4.Dpi = 254F;
            this.xrLabel4.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel4.LocationFloat = new DevExpress.Utils.PointFloat(26.76362F, 294.9869F);
            this.xrLabel4.Multiline = true;
            this.xrLabel4.Name = "xrLabel4";
            this.xrLabel4.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel4.SizeF = new System.Drawing.SizeF(115F, 57F);
            this.xrLabel4.StyleName = "xrControlStyle1";
            this.xrLabel4.Text = "数量:";
            // 
            // lbBoxNumber
            // 
            this.lbBoxNumber.Dpi = 254F;
            this.lbBoxNumber.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "Substring([BoxNumber],Len([BoxNumber])-3 ,3 )")});
            this.lbBoxNumber.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbBoxNumber.LocationFloat = new DevExpress.Utils.PointFloat(673.4078F, 209.4434F);
            this.lbBoxNumber.Multiline = true;
            this.lbBoxNumber.Name = "lbBoxNumber";
            this.lbBoxNumber.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbBoxNumber.SizeF = new System.Drawing.SizeF(293.6799F, 57F);
            this.lbBoxNumber.StyleName = "xrControlStyle1";
            // 
            // xrLabel5
            // 
            this.xrLabel5.Dpi = 254F;
            this.xrLabel5.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel5.LocationFloat = new DevExpress.Utils.PointFloat(558.4078F, 209.4433F);
            this.xrLabel5.Multiline = true;
            this.xrLabel5.Name = "xrLabel5";
            this.xrLabel5.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel5.SizeF = new System.Drawing.SizeF(115F, 57F);
            this.xrLabel5.StyleName = "xrControlStyle1";
            this.xrLabel5.Text = "箱号:";
            // 
            // objectDataSource1
            // 
            this.objectDataSource1.DataSource = typeof(System.Collections.Generic.List<DataAccess.Dto.BatchPrintDto>);
            this.objectDataSource1.Name = "objectDataSource1";
            // 
            // xrControlStyle1
            // 
            this.xrControlStyle1.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrControlStyle1.Name = "xrControlStyle1";
            // 
            // BatchPaperReport
            // 
            this.Bands.AddRange(new DevExpress.XtraReports.UI.Band[] {
            this.TopMargin,
            this.BottomMargin,
            this.Detail});
            this.ComponentStorage.AddRange(new System.ComponentModel.IComponent[] {
            this.objectDataSource1});
            this.DataSource = this.objectDataSource1;
            this.Dpi = 254F;
            this.Font = new System.Drawing.Font("微软雅黑", 10.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.Margins = new System.Drawing.Printing.Margins(0, 0, 0, 0);
            this.PageHeight = 800;
            this.PageWidth = 1001;
            this.PaperKind = System.Drawing.Printing.PaperKind.Custom;
            this.ReportUnit = DevExpress.XtraReports.UI.ReportUnit.TenthsOfAMillimeter;
            this.ShowPrintMarginsWarning = false;
            this.SnapGridSize = 31.75F;
            this.StyleSheet.AddRange(new DevExpress.XtraReports.UI.XRControlStyle[] {
            this.xrControlStyle1});
            this.Version = "21.2";
            ((System.ComponentModel.ISupportInitialize)(this.objectDataSource1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this)).EndInit();

        }

        #endregion

        private DevExpress.XtraReports.UI.TopMarginBand TopMargin;
        private DevExpress.XtraReports.UI.BottomMarginBand BottomMargin;
        private DevExpress.XtraReports.UI.DetailBand Detail;
        private DevExpress.XtraReports.UI.XRLabel xrLabel7;
        private DevExpress.XtraReports.UI.XRLabel xrLabel6;
        private DevExpress.XtraReports.UI.XRLabel lbQty4;
        private DevExpress.XtraReports.UI.XRLabel lbQty3;
        private DevExpress.XtraReports.UI.XRLabel lbQty2;
        private DevExpress.XtraReports.UI.XRLabel xrLabel10;
        private DevExpress.XtraReports.UI.XRBarCode bcBoxNumber;
        private DevExpress.XtraReports.UI.XRLabel lbSkuName;
        private DevExpress.XtraReports.UI.XRLabel xrLabel2;
        private DevExpress.XtraReports.UI.XRLabel lbModel;
        private DevExpress.XtraReports.UI.XRLabel lbDate;
        private DevExpress.XtraReports.UI.XRLabel xrLabel1;
        private DevExpress.XtraReports.UI.XRLabel lbUserName;
        private DevExpress.XtraReports.UI.XRLabel xrLabel3;
        private DevExpress.XtraReports.UI.XRLabel lbQty1;
        private DevExpress.XtraReports.UI.XRLabel xrLabel4;
        private DevExpress.XtraReports.UI.XRLabel lbBoxNumber;
        private DevExpress.XtraReports.UI.XRLabel xrLabel5;
        private DevExpress.DataAccess.ObjectBinding.ObjectDataSource objectDataSource1;
        private DevExpress.XtraReports.UI.XRControlStyle xrControlStyle1;
    }
}
