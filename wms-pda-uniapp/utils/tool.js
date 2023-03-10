import {
	getObjType
} from "@/utils/util";

/**
 * 通用工具类
 */
export default class tool {
	/**
	 * 不为空
	 * @param val
	 * @returns {boolean}
	 */
	static isNotEmpty(val) {
		return !this.isEmpty(val);
	}

	/**
	 * 是否为定义
	 * @param val
	 * @returns {boolean}
	 */
	static isUndefined(val) {
		return val === null || typeof val === 'undefined';
	}

	/**
	 * 为空
	 * @param val
	 * @returns {boolean}
	 */
	static isEmpty(val) {
		return this.isUndefined(val) ||
			(this.isStr(val) && val === '' && val !== 'undefined') ||
			(this.isArray(val) && val.length === 0);
	}

	/**
	 * 对string参数进行判断
	 * 为空，返回默认值
	 * 否则，返回当前值
	 * @param val
	 * @param defaultValue
	 * @returns {string}
	 */
	static strDefault(val, defaultValue) {
		if (this.isEmpty(val)) {
			return defaultValue === undefined ? '' : defaultValue;
		}
		return val;
	}

	/**
	 * 对string参数进行判断
	 * 为空，返回空字符串
	 * 否则，返回当前值
	 * @param val
	 * @returns {string}
	 */
	static strDefaultEmpty(val) {
		return this.strDefault(val, '');
	}

	/**
	 * 强转int型
	 * @param val
	 * @param defaultValue
	 * @returns {number}
	 */
	static toInt(val, defaultValue) {
		if (this.isEmpty(val)) {
			return defaultValue === undefined ? -1 : defaultValue;
		}
		const num = parseInt(val, 0);
		return Number.isNaN(num) ? (defaultValue === undefined ? -1 : defaultValue) : num;
	}

	/**
	 * 强转boolean型
	 * @param val
	 * @returns {boolean}
	 */
	static toBoolean(val) {
		return !!val;
	}

	/**
	 * Json强转为Form类型
	 * @param obj
	 * @returns {FormData}
	 */
	static toFormData(obj) {
		const data = new FormData();
		Object.keys(obj).forEach(key => {
			data.append(key, Array.isArray(obj[key]) ? obj[key].join(',') : obj[key]);
		});
		return data;
	}

	/**
	 * date类转为字符串格式
	 * @param dt 时间
	 * @param args 格式化字符串
	 * @returns {null}
	 */
	static format(dt, args) {
		if (args == 'yyyy-M-d') { // yyyy-M-d
			let year = dt.getFullYear();
			let month = dt.getMonth() + 1;
			let date = dt.getDate();
			return `${year}-${month}-${date}`;
		} else if (args == 'yyyy-M-d H:m:s') { // yyyy-M-d H:m:s
			let year = dt.getFullYear();
			let month = dt.getMonth() + 1;
			let date = dt.getDate();
			let hour = dt.getHours();
			let minute = dt.getMinutes();
			let second = dt.getSeconds();
			return `${year}-${month}-${date} ${hour}:${minute}:${second}`;
		} else if (args == 'yyyy-MM-dd') { // yyyy-MM-dd
			let year = dt.getFullYear();
			let month = (dt.getMonth() + 1).toString().padStart(2, '0');
			let date = dt.getDate().toString().padStart(2, '0');
			return `${year}-${month}-${date}`;
		} else { // yyyy-MM-dd HH:mm:ss
			let year = dt.getFullYear();
			let month = (dt.getMonth() + 1).toString().padStart(2, '0');
			let date = dt.getDate().toString().padStart(2, '0');
			let hour = dt.getHours().toString().padStart(2, '0');
			let minute = dt.getMinutes().toString().padStart(2, '0');
			let second = dt.getSeconds().toString().padStart(2, '0');
			return `${year}-${month}-${date} ${hour}:${minute}:${second}`;
		}
	}

	/**
	 * 根据逗号联合
	 * @param arr
	 * @returns {string}
	 */
	static join(arr) {
		return arr ? arr.join(',') : '';
	}

	/**
	 * 根据逗号分隔
	 * @param str
	 * @returns {string}
	 */
	static split(str) {
		return str ? String(str).split(',') : '';
	}

	/**
	 * 判断对象是否为数组
	 * @param obj
	 * @returns {boolean}
	 */
	static isArray(obj) {
		return Array.isArray(obj);
	}

	/**
	 * 判断对象是否为字符串
	 * @param obj
	 * @returns {boolean}
	 */
	static isStr(obj) {
		return typeof(obj) === 'string';
	}

	/**
	 * 判断对象是否为数值
	 * @param obj
	 * @returns {boolean}
	 */
	static isNumber(obj) {
		return getObjType(obj) === 'number';
	}

	/**
	 * 判断对象是否为Boolean
	 * @param obj
	 * @returns {boolean}
	 */
	static isBoolean(obj) {
		return getObjType(obj) === 'boolean';
	}

	/**
	 * 判断对象是否为Function
	 * @param obj
	 * @returns {boolean}
	 */
	static isFunction(obj) {
		return getObjType(obj) === 'function';
	}

	/**
	 * 判断对象是否为Object
	 * @param obj
	 * @returns {boolean}
	 */
	static isObject(obj) {
		return getObjType(obj) === 'object';
	}

	static isInteger(obj) {
		 var reg = /^[1-9]\d*$/;
		 return reg.test(obj);
	}
	//去除所有空格
	static clearSpace(value) {
		return value.replace(/\s*/g, "");
	}

	static recursionObject(obj, that, target) {
		if (this.isArray(obj)) {
			obj.forEach((item) => {
				this.recursionObject(item, that, target);
			});
		}
		for (let key of Object.keys(obj)) {
			let value = obj[key];
			if (this.isNumber(value) ||
				this.isStr(value) ||
				this.isBoolean(value)
			) {
				that.$set(target, key, value);
			} else if (this.isArray(value)) {
				if (value.length > 0 && this.isObject(value[0])) {
					value.forEach((item) => {
						this.recursionObject(value, that, item);
					})
				} else {
					// 先删除数据内容，然后再加入，保证响应式
					that.form.params[key].splice(0, target[key].length)
					value.forEach((item, index) => {
						that.$set(target[key], index, item);
					})
				}
			} else if (this.isObject(value)) {
				this.recursionObject(value, that, target[key]);
			}
		}
	}
}
