package org.nodes.modules.resource.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Base64Util;
import org.springblade.core.tool.utils.FileUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import sun.misc.BASE64Decoder;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * author: pengwei
 * date: 2021/10/9 13:43
 * description: ResourceController
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/resource")
@Api(value = "资源管理", tags = "资源管理")
public class ResourceController extends BladeController {

	@PostMapping("/put-file")
	public R<Boolean> putFile(@RequestParam MultipartFile file, String name) {
		try {
			ApplicationHome ah = new ApplicationHome(getClass());
			String path = ah.getSource().getParentFile().toString();
			// 删除原有的文件
			List<File> fileList = FileUtil.list(path);
			fileList.forEach(item->{
				String fileName = FileUtil.getNameWithoutExtension(item.getName());
				if (fileName.equals(name)) {
					item.delete();
				}
			});
			String fileName = file.getOriginalFilename();                    // 文件名称
			String fileExt = FileUtil.getFileExtension(fileName);            // 文件扩展名
			String fileFullPath = path + File.separator + name + "." + fileExt;
			file.transferTo(new File(fileFullPath));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return R.data(true);
	}

	@GetMapping("/get-file")
	public R<String> getFile(@RequestParam String name) {
		String base64Str = StringPool.EMPTY;
		try {
			ApplicationHome ah = new ApplicationHome(getClass());
			String path = ah.getSource().getParentFile().toString();
			List<File> fileList = FileUtil.list(path);
			for (File file : fileList) {
				String fileName = FileUtil.getNameWithoutExtension(file.getName());
				if (!fileName.equals(name)) {
					continue;
				}
				base64Str = Base64Util.encodeToString(FileUtil.readToByteArray(file));
				break;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return R.data(base64Str);
	}
}
