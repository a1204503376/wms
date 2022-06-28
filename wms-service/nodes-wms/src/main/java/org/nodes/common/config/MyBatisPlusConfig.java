package org.nodes.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.springblade", "org.nodes"})
@MapperScan({"org.springblade.**.mapper.**", "org.nodes.**.mapper.**"})
public class MyBatisPlusConfig {

	/**
	 * MybatisPlus插件Bean
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(
			// 新的分页插件,一缓和二缓遵循mybatis的规则
			new PaginationInnerInterceptor(DbType.MYSQL)
		);
		interceptor.addInnerInterceptor(
			// 乐观锁插件
			new OptimisticLockerInnerInterceptor()
		);
		return interceptor;
	}

}
