import {menuMixin} from "@/mixins/menu";
import func from "@/util/func";
import {getCrudColumnResponseList} from "@/api/core/column";
import {deepClone} from "@/util/util";
import {mapGetters} from "vuex";
import item from "@/views/wms/billing/item";

export const listMixin = {
    mixins: [menuMixin],
    data() {
        return {
            masterConfig: {},
            form: {
                params: {},
                events: {
                    search: this.onSearch,
                    reset: this.onReset
                }
            },
            table: {
                columnList: [],
                data: []
            },
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
            }
        }
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionObj(){
            return {}
        }
    },
    created() {
        this.getCrudColumnList();
    },
    methods: {
        getTableData() {
        },
        onSearch() {
            this.getTableData();
        },
        onReset() {
            this.restoreDefaults();
        },
        restoreDefaults(){
            function recursionObject(obj) {
                for (let key of Object.keys(obj)) {
                    let value = obj[key];
                    if (func.isNumber(value)) {
                        obj[key] = 0;
                    } else if (func.isArray(value)) {
                        obj[key] = [];
                    } else if (func.isStr(value)) {
                        obj[key] = '';
                    } else if (func.isBoolean(value)) {
                        obj[key] = false;
                    } else if (func.isObject(value)) {
                        recursionObject(value);
                    }
                }
            }
            recursionObject(this.form.params);
        },
        onRefresh() {
            this.getTableData();
        },
        onSortChange(column, prop, order) {
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
            columnList.forEach(d => {
                let find = data.find(m => m['prop'] === d['prop']);
                if (flag === 'configColumn') {
                    d['aliasName'] = find['aliasName'];
                    d['sort'] = find['sort'];
                } else {
                    d['label'] = func.strDefault(find['aliasName'], d['label']);
                }
                d['width'] = func.toInt(find['width'], 0);
                d['hide'] = find['hide'];
                d['fixed'] = find['fixed'];

            });
        },
        getCrudColumnList() {
            let menu = this.getMenu();
            getCrudColumnResponseList(menu.id)
                .then((response) => {
                    let {data} = response.data;
                    let deepCloneColumnList = deepClone(this.table.columnList);
                    if (func.isNotEmpty(data)) {
                        this.setColumnList(deepCloneColumnList, data, 'configColumn');
                        this.setColumnList(this.table.columnList, data);
                    }
                    this.columnShowHide.dataSource = deepCloneColumnList;
                });
        },
        onColumnShowHide(column) {
            this.columnShowHide.visible = !this.columnShowHide.visible;
            !this.columnShowHide.visible && this.updateColumn(column);
        },
        updateColumn(columnObj) {
            let crudColumn = this.$store.getters.crudColumn;
            let menu = this.getMenu();
            if (!menu) {
                return;
            }
            let column = columnObj || crudColumn.find(u => {
                return u.menuId = menu.id;
            });
            if (!column || !column.columnList) {
                return;
            }
            this.setColumnList(this.table.columnList, column.columnList);
            this.table.columnList.sort((a, b) => {
                let x = a['sort'], y = b['sort'];
                return ((x < y) ? -1 : (x > y) ? 1 : 0);
            });
            this.$nextTick(() => {
                this.$refs.table.doLayout();
            });
        }
    }
}
