import { NgModule } from '@angular/core';
import { NoDblClickDirective } from './debounce-click/no-dbl-click';
@NgModule({
	declarations: [NoDblClickDirective],
	imports: [],
	exports: [NoDblClickDirective]
})
export class DirectivesModule { }
