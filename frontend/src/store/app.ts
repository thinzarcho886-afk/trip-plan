import { defineStore } from 'pinia';
import { AppTheme } from '../plugins/vuetify';

export const useAppStore = defineStore('app', {
  state: () => ({
    theme: 'light' as AppTheme,
    bookingData: null as any,
  }),
  actions: {
    changeAppTheme(value: AppTheme) {
      this.theme = value;
    },
    setBookingData(data: any) {
      this.bookingData = data;
    },
  },
});