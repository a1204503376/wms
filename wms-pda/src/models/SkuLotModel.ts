export class SkuLotModel {
    skulot1: string = '';
    skulot2: string = '';
    skulot3: string = '';
    skulot4: string = '';
    skulot5: string = '';
    skulot6: string = '';
    skulot7: string = '';
    skulot8: string = '';
    skulot9: string = '';
    skulot10: string = '';
    skulot11: string = '';
    skulot12: string = '';
    skulot13: string = '';
    skulot14: string = '';
    skulot15: string = '';
    skulot16: string = '';
    skulot17: string = '';
    skulot18: string = '';
    skulot19: string = '';
    skulot20: string = '';
    skulot21: string = '';
    skulot22: string = '';
    skulot23: string = '';
    skulot24: string = '';
    skulot25: string = '';
    skulot26: string = '';
    skulot27: string = '';
    skulot28: string = '';
    skulot29: string = '';
    skulot30: string = '';
    skuLotName1: string = '';
    skuLotName2: string = '';
    skuLotName3: string = '';
    skuLotName4: string = '';
    skuLotName5: string = '';
    skuLotName6: string = '';
    skuLotName7: string = '';
    skuLotName8: string = '';
    skuLotName9: string = '';
    skuLotName10: string = '';
    skuLotName11: string = '';
    skuLotName12: string = '';
    skuLotName13: string = '';
    skuLotName14: string = '';
    skuLotName15: string = '';
    skuLotName16: string = '';
    skuLotName17: string = '';
    skuLotName18: string = '';
    skuLotName19: string = '';
    skuLotName20: string = '';
    skuLotName21: string = '';
    skuLotName22: string = '';
    skuLotName23: string = '';
    skuLotName24: string = '';
    skuLotName25: string = '';
    skuLotName26: string = '';
    skuLotName27: string = '';
    skuLotName28: string = '';
    skuLotName29: string = '';
    skuLotName30: string = '';
    set setLotModel(model) {
        for (let index = 0; index < model.length; index++) {
            let indexExt = index + 1;
            this.setLotValue(indexExt, model[index]);
        }
    }
    setLotValue(index, value) {
        switch (index) {
            case 1: this.skulot1 = value; break;
            case 2: this.skulot2 = value; break;
            case 3: this.skulot3 = value; break;
            case 4: this.skulot4 = value; break;
            case 5: this.skulot5 = value; break;
            case 6: this.skulot6 = value; break;
            case 7: this.skulot7 = value; break;
            case 8: this.skulot8 = value; break;
            case 9: this.skulot9 = value; break;
            case 10: this.skulot10 = value; break;
            case 11: this.skulot11 = value; break;
            case 12: this.skulot12 = value; break;
            case 13: this.skulot13 = value; break;
            case 14: this.skulot14 = value; break;
            case 15: this.skulot15 = value; break;
            case 16: this.skulot16 = value; break;
            case 17: this.skulot17 = value; break;
            case 18: this.skulot18 = value; break;
            case 19: this.skulot19 = value; break;
            case 20: this.skulot20 = value; break;
            case 21: this.skulot21 = value; break;
            case 22: this.skulot22 = value; break;
            case 23: this.skulot23 = value; break;
            case 24: this.skulot24 = value; break;
            case 25: this.skulot25 = value; break;
            case 26: this.skulot26 = value; break;
            case 27: this.skulot27 = value; break;
            case 28: this.skulot28 = value; break;
            case 29: this.skulot29 = value; break;
            case 30: this.skulot30 = value; break;
        }
    }
}