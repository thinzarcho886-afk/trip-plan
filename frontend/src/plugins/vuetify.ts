import { createVuetify, ThemeDefinition } from 'vuetify';
import 'vuetify/styles';
import { aliases, mdi } from 'vuetify/iconsets/mdi-svg';
// import { md3 } from 'vuetify/blueprints';

export enum AppTheme {
  LIGHT = 'light',
  DARK = 'dark',
  CUSTOM = 'custom',
}

const light: ThemeDefinition = {
  dark: false,
  colors: {
    primary: '#9CC477', // #1867C0 //#800080
    secondary: '#D1E4C4',//#5CBBF6
  },
};

const dark: ThemeDefinition = {
  dark: true,
  colors: {
    primary: '#1867C0',
    secondary: '#5CBBF6',
  },
};

export default createVuetify({
  // blueprint: md3,

  theme: {
    defaultTheme: 'light',
    themes: {
      light,
    },
  },
  icons: {
    defaultSet: 'mdi',
    aliases: {
      ...aliases,
    },
    sets: {
      mdi,
    },
  },
  defaults: {
    VTextField: { variant: 'outlined', density: 'comfortable' },
    VSelect: { variant: 'outlined', density: 'comfortable' },
    VSwitch: {
      inset: true,
      density: 'comfortable',
      falseValue: false,
      trueValue: true,
      color: 'primary',
    },
    VTextarea: { variant: 'outlined', density: 'comfortable' },
  },
});
