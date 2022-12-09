namespace Packaging.Encasement.Reports
{
    partial class SerialPaperReport
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
            this.xrLabel9 = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel13 = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel10 = new DevExpress.XtraReports.UI.XRLabel();
            this.bcBoxNumber = new DevExpress.XtraReports.UI.XRBarCode();
            this.lbSkuName = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel2 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbModel = new DevExpress.XtraReports.UI.XRLabel();
            this.lbDate = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel1 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbQty = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel4 = new DevExpress.XtraReports.UI.XRLabel();
            this.lbBoxNumber = new DevExpress.XtraReports.UI.XRLabel();
            this.xrLabel5 = new DevExpress.XtraReports.UI.XRLabel();
            this.objectDataSource1 = new DevExpress.DataAccess.ObjectBinding.ObjectDataSource(this.components);
            ((System.ComponentModel.ISupportInitialize)(this.objectDataSource1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this)).BeginInit();
            // 
            // TopMargin
            // 
            this.TopMargin.Dpi = 254F;
            this.TopMargin.HeightF = 3F;
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
            this.xrLabel9,
            this.xrLabel13,
            this.xrLabel10,
            this.bcBoxNumber,
            this.lbSkuName,
            this.xrLabel2,
            this.lbModel,
            this.lbDate,
            this.xrLabel1,
            this.lbQty,
            this.xrLabel4,
            this.lbBoxNumber,
            this.xrLabel5});
            this.Detail.Dpi = 254F;
            this.Detail.HeightF = 699.7169F;
            this.Detail.HierarchyPrintOptions.Indent = 50.8F;
            this.Detail.Name = "Detail";
            // 
            // xrLabel9
            // 
            this.xrLabel9.AutoWidth = true;
            this.xrLabel9.Dpi = 254F;
            this.xrLabel9.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[BoxNumber]")});
            this.xrLabel9.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel9.LocationFloat = new DevExpress.Utils.PointFloat(265.5219F, 574.2042F);
            this.xrLabel9.Multiline = true;
            this.xrLabel9.Name = "xrLabel9";
            this.xrLabel9.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel9.SizeF = new System.Drawing.SizeF(374.5804F, 57F);
            this.xrLabel9.StylePriority.UseFont = false;
            this.xrLabel9.StylePriority.UseTextAlignment = false;
            this.xrLabel9.TextAlignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            // 
            // xrLabel13
            // 
            this.xrLabel13.Dpi = 254F;
            this.xrLabel13.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Udf2]")});
            this.xrLabel13.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel13.LocationFloat = new DevExpress.Utils.PointFloat(661.6317F, 459.2691F);
            this.xrLabel13.Multiline = true;
            this.xrLabel13.Name = "xrLabel13";
            this.xrLabel13.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel13.SizeF = new System.Drawing.SizeF(275.4912F, 57F);
            this.xrLabel13.StylePriority.UseFont = false;
            // 
            // xrLabel10
            // 
            this.xrLabel10.Dpi = 254F;
            this.xrLabel10.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[SpecialCustomer]")});
            this.xrLabel10.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel10.LocationFloat = new DevExpress.Utils.PointFloat(661.6317F, 355.5774F);
            this.xrLabel10.Multiline = true;
            this.xrLabel10.Name = "xrLabel10";
            this.xrLabel10.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel10.SizeF = new System.Drawing.SizeF(275.4912F, 57F);
            this.xrLabel10.StylePriority.UseFont = false;
            // 
            // bcBoxNumber
            // 
            this.bcBoxNumber.Alignment = DevExpress.XtraPrinting.TextAlignment.MiddleCenter;
            this.bcBoxNumber.AutoModule = true;
            this.bcBoxNumber.Dpi = 254F;
            this.bcBoxNumber.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[BoxNumberLabel]")});
            this.bcBoxNumber.LocationFloat = new DevExpress.Utils.PointFloat(265.5219F, 279.0804F);
            this.bcBoxNumber.Module = 10.16F;
            this.bcBoxNumber.Name = "bcBoxNumber";
            this.bcBoxNumber.Padding = new DevExpress.XtraPrinting.PaddingInfo(25, 25, 0, 0, 254F);
            this.bcBoxNumber.ShowText = false;
            this.bcBoxNumber.SizeF = new System.Drawing.SizeF(374.5804F, 295.1237F);
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
            this.lbSkuName.Font = new System.Drawing.Font("微软雅黑", 15F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
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
            this.xrLabel2.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel2.LocationFloat = new DevExpress.Utils.PointFloat(61.34408F, 123.4359F);
            this.xrLabel2.Name = "xrLabel2";
            this.xrLabel2.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel2.SizeF = new System.Drawing.SizeF(142F, 56.99999F);
            this.xrLabel2.StylePriority.UseFont = false;
            this.xrLabel2.Text = "型号：";
            // 
            // lbModel
            // 
            this.lbModel.AutoWidth = true;
            this.lbModel.Dpi = 254F;
            this.lbModel.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[Model]")});
            this.lbModel.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbModel.LocationFloat = new DevExpress.Utils.PointFloat(203.3905F, 123.4359F);
            this.lbModel.Multiline = true;
            this.lbModel.Name = "lbModel";
            this.lbModel.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbModel.SizeF = new System.Drawing.SizeF(266F, 57F);
            this.lbModel.StylePriority.UseFont = false;
            // 
            // lbDate
            // 
            this.lbDate.Dpi = 254F;
            this.lbDate.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[PrintDate]")});
            this.lbDate.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbDate.LocationFloat = new DevExpress.Utils.PointFloat(688.6317F, 123.4359F);
            this.lbDate.Multiline = true;
            this.lbDate.Name = "lbDate";
            this.lbDate.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbDate.SizeF = new System.Drawing.SizeF(266F, 56.99999F);
            this.lbDate.StylePriority.UseFont = false;
            // 
            // xrLabel1
            // 
            this.xrLabel1.Dpi = 254F;
            this.xrLabel1.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel1.LocationFloat = new DevExpress.Utils.PointFloat(546.6317F, 123.4359F);
            this.xrLabel1.Name = "xrLabel1";
            this.xrLabel1.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel1.SizeF = new System.Drawing.SizeF(142F, 57F);
            this.xrLabel1.StylePriority.UseFont = false;
            this.xrLabel1.Text = "日期：";
            // 
            // lbQty
            // 
            this.lbQty.Dpi = 254F;
            this.lbQty.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "[QtyLabel]")});
            this.lbQty.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbQty.LocationFloat = new DevExpress.Utils.PointFloat(203.3441F, 198.4527F);
            this.lbQty.Multiline = true;
            this.lbQty.Name = "lbQty";
            this.lbQty.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbQty.SizeF = new System.Drawing.SizeF(266F, 57F);
            this.lbQty.StylePriority.UseFont = false;
            // 
            // xrLabel4
            // 
            this.xrLabel4.Dpi = 254F;
            this.xrLabel4.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel4.LocationFloat = new DevExpress.Utils.PointFloat(61.34407F, 198.4527F);
            this.xrLabel4.Name = "xrLabel4";
            this.xrLabel4.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel4.SizeF = new System.Drawing.SizeF(142F, 57F);
            this.xrLabel4.StylePriority.UseFont = false;
            this.xrLabel4.Text = "数量：";
            // 
            // lbBoxNumber
            // 
            this.lbBoxNumber.Dpi = 254F;
            this.lbBoxNumber.ExpressionBindings.AddRange(new DevExpress.XtraReports.UI.ExpressionBinding[] {
            new DevExpress.XtraReports.UI.ExpressionBinding("BeforePrint", "Text", "Substring([BoxNumber],Len([BoxNumber])-3 ,3)\n")});
            this.lbBoxNumber.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.lbBoxNumber.LocationFloat = new DevExpress.Utils.PointFloat(688.6317F, 198.4527F);
            this.lbBoxNumber.Multiline = true;
            this.lbBoxNumber.Name = "lbBoxNumber";
            this.lbBoxNumber.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.lbBoxNumber.SizeF = new System.Drawing.SizeF(266F, 56.99997F);
            this.lbBoxNumber.StylePriority.UseFont = false;
            // 
            // xrLabel5
            // 
            this.xrLabel5.Dpi = 254F;
            this.xrLabel5.Font = new System.Drawing.Font("微软雅黑", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.xrLabel5.LocationFloat = new DevExpress.Utils.PointFloat(546.6317F, 198.4527F);
            this.xrLabel5.Name = "xrLabel5";
            this.xrLabel5.Padding = new DevExpress.XtraPrinting.PaddingInfo(5, 5, 0, 0, 254F);
            this.xrLabel5.SizeF = new System.Drawing.SizeF(142F, 57F);
            this.xrLabel5.StylePriority.UseFont = false;
            this.xrLabel5.Text = "箱号：";
            // 
            // objectDataSource1
            // 
            this.objectDataSource1.DataSource = typeof(System.Collections.Generic.List<DataAccess.Dto.SerialNumberPrintDto>);
            this.objectDataSource1.Name = "objectDataSource1";
            // 
            // SerialPaperReport
            // 
            this.Bands.AddRange(new DevExpress.XtraReports.UI.Band[] {
            this.TopMargin,
            this.BottomMargin,
            this.Detail});
            this.ComponentStorage.AddRange(new System.ComponentModel.IComponent[] {
            this.objectDataSource1});
            this.DataSource = this.objectDataSource1;
            this.Dpi = 254F;
            this.Font = new System.Drawing.Font("微软雅黑", 10.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.Margins = new System.Drawing.Printing.Margins(0, 0, 3, 0);
            this.PageHeight = 700;
            this.PageWidth = 1000;
            this.PaperKind = System.Drawing.Printing.PaperKind.Custom;
            this.ReportUnit = DevExpress.XtraReports.UI.ReportUnit.TenthsOfAMillimeter;
            this.Scripts.OnParametersRequestValueChanged = "SerialNumberReport_ParametersRequestValueChanged";
            this.ShowPrintMarginsWarning = false;
            this.SnapGridSize = 31.75F;
            this.Version = "21.2";
            ((System.ComponentModel.ISupportInitialize)(this.objectDataSource1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this)).EndInit();

        }

        #endregion

        private DevExpress.XtraReports.UI.TopMarginBand TopMargin;
        private DevExpress.XtraReports.UI.BottomMarginBand BottomMargin;
        private DevExpress.XtraReports.UI.DetailBand Detail;
        private DevExpress.XtraReports.UI.XRLabel xrLabel13;
        private DevExpress.XtraReports.UI.XRLabel xrLabel10;
        private DevExpress.XtraReports.UI.XRBarCode bcBoxNumber;
        private DevExpress.XtraReports.UI.XRLabel lbSkuName;
        private DevExpress.XtraReports.UI.XRLabel xrLabel2;
        private DevExpress.XtraReports.UI.XRLabel lbModel;
        private DevExpress.XtraReports.UI.XRLabel lbDate;
        private DevExpress.XtraReports.UI.XRLabel xrLabel1;
        private DevExpress.XtraReports.UI.XRLabel lbQty;
        private DevExpress.XtraReports.UI.XRLabel xrLabel4;
        private DevExpress.XtraReports.UI.XRLabel lbBoxNumber;
        private DevExpress.XtraReports.UI.XRLabel xrLabel5;
        private DevExpress.DataAccess.ObjectBinding.ObjectDataSource objectDataSource1;
        private DevExpress.XtraReports.UI.XRLabel xrLabel9;
    }
}
