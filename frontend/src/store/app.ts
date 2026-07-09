import { defineStore } from 'pinia';
import { AppTheme } from '../plugins/vuetify';

export const useAppStore = defineStore('app', {
  state: () => ({
    theme: 'light',
  }),
  actions: {
    changeAppTheme(value: AppTheme) {
      this.theme = value;
    },
  },
});
