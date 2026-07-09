import { createApp } from 'vue';
import App from './App.vue';

import './assets/scss/style.scss';
import{
mdiPhone,


} from '@mdi/js';
// plugins
import { registerPlugins } from './plugins';
const app = createApp(App);

registerPlugins(app);

app.mount('#app');
