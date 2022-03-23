export function getHide(col) {
    if (col.hide !== undefined) {
        if (typeof col.hide === 'function') {
            return col.hide(this.form, this.editType);
        } else {
            return !col.hide;
        }
    } else {
        return true;
    }
}
