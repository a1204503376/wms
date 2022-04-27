import {menuMixin} from "@/mixins/menu";
import func from "@/util/func";
import {getCrudColumnResponseList} from "@/api/core/column";
import {deepClone} from "@/util/util";
import {mapGetters} from "vuex";

export const listMixin = {
    mixins: [menuMixin],
    data() {
        return {
            masterConfig: {},
            searchForm: {

            },
            table: {
                columnList: [],
                data: []
            },
            page: {
                total: 0,
                pageSize: 20,
                currentPage: 1,
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
        permissionList() {
            return {
            };
        },
        hideOnSinglePage: function () {
            return this.page.total <= this.page.pageSize
        }
    },
    created() {
        this.getCrudColumnList();
    },
    methods: {
        getTableData() {
            // API调用:post(this.searchFrom)
            function getRandomInt(min, max) {
                min = Math.ceil(min);
                max = Math.floor(max);
                return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
            }

            let fill = [];
            for (let i = 0; i < 101; i++) {
                // 模拟表格数据
                let item = {
                    date: `${getRandomInt(2018, 2022)}-${getRandomInt(1, 12)}-${getRandomInt(1, 28)}`,
                    name: "王小虎" + getRandomInt(1, 101),
                    wages: getRandomInt(3000, 15000),
                    address: `上海市普陀区金沙江路 ${getRandomInt(100, 2000)} 弄`
                };
                fill.push(item);
            }
            let length = fill.length;
            this.page.total = length;
            let offset = (this.page.currentPage - 1) * this.page.pageSize;
            let number = offset + this.page.pageSize;
            this.table.data = (number >= length)
                ? fill.slice(offset, length)
                : fill.slice(offset, number);
        },
        onRefresh() {
            this.getTableData();
        },
        onSubmit() {
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
            this.page.pageSize = val;
            this.getTableData();
        },
        handleCurrentChange(val) {
            this.page.currentPage = val;
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
