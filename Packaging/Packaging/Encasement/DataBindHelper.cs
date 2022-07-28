using DataAccess.Encasement;
using DataAccess.Wms;
using DevExpress.XtraEditors.Repository;

namespace Packaging.Encasement
{
    public class DataBindHelper
    {
        public static void SetSkuDataSource(RepositoryItemSearchLookUpEdit repositoryItemSearchLookUpEdit)
        {
            var skus = SkuDal.GetAll();
            repositoryItemSearchLookUpEdit.DisplayMember = "SkuName";
            repositoryItemSearchLookUpEdit.KeyMember = "SkuId";
            repositoryItemSearchLookUpEdit.DataSource = skus;
        }

        public static void SetSpeedClassDataSource(RepositoryItemSearchLookUpEdit repositoryItemSearchLookUpEdit)
        {
            var packingSpeedClasses = SpeedClassDal.GetAll();
            repositoryItemSearchLookUpEdit.DisplayMember = "SpeedClass";
            repositoryItemSearchLookUpEdit.KeyMember = "Id";
            repositoryItemSearchLookUpEdit.DataSource = packingSpeedClasses;
        }
    }
}