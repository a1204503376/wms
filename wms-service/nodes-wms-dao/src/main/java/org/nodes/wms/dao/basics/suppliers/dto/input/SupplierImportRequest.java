package org.nodes.wms.dao.basics.suppliers.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 供应商导入请求对象
 **/
@Data
public class SupplierImportRequest implements Serializable {

	private static final long serialVersionUID = 6000689152261692317L;

	/**
	 * 新增供应商对象集合
	 */
	@NotNull(message = "文件中还没有数据")
	private List<AddSupplierRequest> addSupplierList;

	/**
	 * 导入的文件
	 */
//	MultipartFile file;
}
