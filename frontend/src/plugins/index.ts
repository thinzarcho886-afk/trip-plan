// Plugins
import vuetify from './vuetify';
import router from '../router';
import pinia from '../store';
import  { i18n }  from './i18n';

// Types
import type { App } from 'vue';

export function registerPlugins(app: App) {
  app.use(pinia).use(vuetify).use(router).use(i18n);
}
