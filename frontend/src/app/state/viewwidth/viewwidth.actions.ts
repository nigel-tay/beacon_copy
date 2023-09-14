import { createAction } from "@ngrx/store";

export const isMobile = createAction('[ViewWidth Component] IsMobile');
export const isDesktop = createAction('[ViewWidth Component] IsDesktop');
export const isTablet = createAction('[ViewWidth Component] IsTablet');