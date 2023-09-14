import { createReducer, on } from '@ngrx/store';
import { isMobile, isTablet, isDesktop } from './viewwidth.actions';

export const initialState: number = 1024

export const viewwidthReducer = createReducer (
    initialState,
    on(isMobile, (state) => 640),
    on(isTablet, (state) => 768),
    on(isDesktop, (state) => 1024)
)