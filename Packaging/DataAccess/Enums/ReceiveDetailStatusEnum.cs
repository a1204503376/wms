namespace DataAccess.Enums
{
    public enum ReceiveDetailStatusEnum
    {
        /**
	     *未收货
	     */
        NotReceipt = 10,

        /**
		 * 部分收货
		 */
        Part = 20,

        /**
		 * 全部收货
		 */
        Completed = 30,

        /**
		 * 已取消
		 */
        Cancel = 90

    }
}