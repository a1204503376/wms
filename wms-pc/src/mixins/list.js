import {menuMixin} from "@/mixins/menu";
import func from "@/util/func";
import {nowDateFormat} from "@/util/date"
import {getCrudColumnResponseList} from "@/api/core/column";
import {deepClone} from "@/util/util";
import {mapGetters} from "vuex";
import {getStore, setStore} from '@/util/store';

export const listMixin = {
    mixins: [menuMixin],
    data() {
        return {
            form: {
                params: {},
                deepCloneParams: {},
                events: {
                    search: this.onSearch,
                    reset: this.onReset,
                    changeTableHeight: this.handleTableHeightChange,
                }
            },
            table: {
                columnList: [],
                data: [],
                height: 300
            },
            crudColumn: getStore({name: "crudColumn"}) || [],
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            columnShowHide: {
                visible: false,
                dataSource: []
            },
            exportExcelName: '',
            exportExcelSheet: [
                {
                    tHeader: [],
                    table: [],
                    keys: [],
                    sheetName: "",
                    cellStyle: [],
                }
            ]
        }
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionObj() {
            return {
                search: true
            }
        }
    },
    watch: {
        $route() {
            this.handleRefreshTable();
        }
    },
    created() {
        this.getCrudColumnList();
        this.copyInitialValue();
    },
    methods: {
        handleRefreshTable() {
            // 解决固定列错位的问题
            this.$nextTick(() => {
                if (this.$refs.table && this.$refs.table.doLayout) {
                    this.$refs.table.doLayout();
                }
            });
        },
        async copyInitialValue() {
            this.form.deepCloneParams = deepClone(this.form.params);
        },
        getTableData() {
        },
        onSearch() {
            this.page.current = 1;
            this.getTableData();
        },
        onReset() {
            this.restoreDefaults();
        },
        restoreDefaults() {
            func.recursionObject(deepClone(this.form.deepCloneParams), this, this.form.params);
        },
        onRefresh() {
            this.getTableData();
        },
        handleTableHeightChange(data) {
            this.table.height = data;
            this.handleRefreshTable();
        },
        onSortChange(column) {
            let prop = column.prop;
            let order = column.order;
            if (order === "ascending") {
                this.page.ascs = prop;
                this.page.descs = "";
            } else if (order === "descending") {
                this.page.descs = prop;
                this.page.ascs = "";
            } else {
                this.page.ascs = "";
                this.page.descs = "";
            }
            this.getTableData();
        },
        handleSizeChange(val) {
            this.page.size = val;
            this.getTableData();
        },
        handleCurrentChange(val) {
            this.page.current = val;
            this.getTableData();
        },
        setColumnList(columnList, data, flag) {
            if (func.isEmpty(data)) {
                return;
            }
            let deepCloneColumnList = deepClone(data);
            // this.columnShowHide.dataSource = deepClone(data);
            deepCloneColumnList.forEach(d => {
                let find = data.find(m => m['prop'] === d['prop']);
                if (!find) {
                    return;
                }
                Object.assign(d, {
                    aliasName: find.aliasName,
                    width: func.toInt(find.width || 120, 0),
                    hide: find.hide,
                    fixed: find.fixed,
                    align: find.align,
                    order: find.order
                });
            });
            // 显隐列组件赋值的列名称已本地配置的label为主，优先处理
            this.columnShowHide.dataSource = deepClone(deepCloneColumnList);
            // 2. 再处理本地label是否采用别名
            deepCloneColumnList.forEach(d => {
                let find = data.find(m => m['prop'] === d['prop']);
                if (!find) {
                    return;
                }
                Object.assign(d, {
                    label: func.strDefault(find.aliasName, d.label)
                });
            });
            func.recursionObject(deepCloneColumnList, this, this.table.columnList);
            // this.table.columnList.sort((a, b) => {
            //     let x = a['order'], y = b['order'];
            //     return ((x < y) ? -1 : (x > y) ? 1 : 0);
            // });
        },
        async getCrudColumnList() {
            let self = this;
            let menus = this.getMenu();
            let menu = {};
            let crudMenu = {};
            menus.children.forEach(function (item, index) {
                if (item.path == self.$route.path) {
                    menu = item;
                }
            });
            let crudColumn = getStore({name: "crudColumn"});
            if (func.isNotEmpty(crudColumn)) {
                crudColumn.forEach(function (item, index) {
                    if (JSON.stringify(item.menuId) === JSON.stringify(menu.id) && getStore({name: "userInfo"}).user_id === item.userId) {
                        crudMenu = item.columnList;
                        self.table.columnList = item.columnList;
                    }
                });

            }
            if (func.isEmpty(crudMenu.length)) {
                await getCrudColumnResponseList(menu.id)
                    .then(({data: {data}}) => {
                        if (data.length != 0) {
                            // 缓存本地的数据格式
                            let column = {
                                menuId: menu.id,
                                columnList: data,
                                userId: getStore({name: "userInfo"}).user_id
                            }
                            let index = self.crudColumn.findIndex(u => {
                                return u.menuId === menu.id && u.userId === getStore({name: "userInfo"}).user_id;
                            });
                            if (index < 0) {
                                self.crudColumn.push(column);
                            } else {
                                self.crudColumn.splice(index, 1, column);
                            }
                            setStore({name: 'crudColumn', content: self.crudColumn, type: 'session'});
                            self.loading.content = false;
                            self.loading.saveBtn = false;
                            crudMenu = data;
                        } else {
                            crudMenu = this.table.columnList;
                        }
                    });
            }
            this.setColumnList(this.table.columnList, crudMenu);
            return self.table.columnList;
        },
        onColumnShowHide(column) {
            this.columnShowHide.visible = !this.columnShowHide.visible;
            !this.columnShowHide.visible && this.updateColumn(column);
        },
        updateColumn(columnObj) {
            if (!func.isObject(columnObj)
                || func.isEmpty(columnObj['columnList'])) {
                return;
            }
            this.setColumnList(this.getCrudColumnList(), columnObj.columnList);
        },
        // 当前页导出
        exportCurrentDataToExcel(sheetName, filename) {
            this.exportExcelSheet[0].cellStyle = [];
            this.exportExcelSheet[0].tHeader = this.table.columnList.map((item) => {
                return item.label;
            }).join(",").split(",");
            this.exportExcelSheet[0].table = this.table.data;
            this.exportExcelSheet[0].keys = this.table.columnList.map((item) => {
                return item.prop;
            }).join(",").split(",");
            this.exportExcelSheet[0].sheetName = sheetName || "sheet1"
            let localStr = nowDateFormat("yyyyMMddhhmm")
            this.exportExcelName = filename + localStr || localStr;
            this.exportExcelSheet[0].tHeader.forEach((value, index) => {
                let result = "";
                let num = index <= 0 ? 1 : index + 1;//小于等于0是输出A
                while (num > 0) {
                    let m = num % 26;
                    if (m === 0) {
                        m = 26;
                    }
                    result = String.fromCharCode(m + 64) + result;
                    num = (num - m) / 26;
                }
                let cell = {
                    cell: result + '1', //表头 A1、B1、C1、D1......AA1、AB1、AC1、AD1......BA1、BB1......
                    font: {
                        name: '宋体',
                        sz: 14,
                        bold: true,
                    },
                    fill: {
                        fgColor: {rgb: "c0c0c0"},
                    }
                };
                this.exportExcelSheet[0].cellStyle.push(cell);
            })
        },
        onUpload() {
            this.fileUpload.visible = true;
        },
        getFormData(res) {
            let file = res.data.localFile.file;
            let param = new FormData();
            param.append("file", file);
            return param;
        }
    }
}
