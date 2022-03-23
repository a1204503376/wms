import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({ selector: '[no-dbl-click]' })
export class NoDblClickDirective {

  //可选传参highlightColor
  @Input('no-dbl-click') highlightColor: string;
  // public defaultColor = 'pink';//默认背景颜色

  constructor(public el: ElementRef) {
    //实现设置默认背景色
    // this.el.nativeElement.style.backgroundColor = this.defaultColor;
  }
  //监听注解写在某个方法上面，表示下面的方法是该事件处理函数
  //实现不能重复点击该按钮(设置disabled属性为true,3秒后改为false)
  // @HostListener('click')
  // click() {
  //   this.el.nativeElement.disabled = true;
  //   setTimeout(() => {
  //     this.el.nativeElement.disabled = false;
  //   }, 1000)
  // }
  //手指按下去的时候触发该监听事件，设置背景色
  @HostListener('touchstart')
  onTouchStart() {
    this.el.nativeElement.disabled = true;
    setTimeout(() => {
      this.el.nativeElement.disabled = false;
    }, 1000)
  }
  //手指离开的时候触发该监听事件，取消背景色
  // @HostListener('touchend')
  // onTouchsEnd() {
  //   debugger;
  // }
  //监听回车事件
  // @HostListener('window:keydown', ['$event'])
  // handleKeyDown(event: KeyboardEvent) {
  //   if (event.key === 'Enter') {
  //     //在这里执行查询事件
  //     this.el.nativeElement.disabled = true;
  //     setTimeout(() => {
  //       this.el.nativeElement.disabled = false;
  //     }, 500)
  //   }
  // }



  private highlight(color: string) {
    this.el.nativeElement.style.backgroundColor = color;
  }
}