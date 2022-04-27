export const menuMixin = {
    methods: {
        getMenuByRecursion(menuAll, route) {
            return menuAll.find(u => {
                if (u.name === route.name && u.path === route.path) {
                    return u;
                } else if (u.children && u.children.length > 0) {
                    return this.getMenuByRecursion(u.children, route);
                }
            });
        },
        getMenu() {
            let menuAll = this.$store.getters.menuAll;
            return this.getMenuByRecursion(menuAll, this.$route);
        }
    }
}
