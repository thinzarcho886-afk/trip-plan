import { createI18n } from 'vue-i18n';
import messages from '@intlify/unplugin-vue-i18n/messages';

const LOCALE_KEY = import.meta.env.VITE_APP_LOCALSTORAGE_PREFIX + 'app:locale';

// List of all locales.
export const availableLocales = [
  { text: 'English', value: 'en' },
  { text: 'Myanmar', value: 'mm' },
];
const defaultLocale = localStorage.getItem(LOCALE_KEY) ?? 'en';
const fallbackLocale = import.meta.env.VITE_APP_I18N_FALLBACK_LOCALE;

// Create Vue I18n instance.
export const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: defaultLocale,
  fallbackLocale: fallbackLocale,
  messages: messages,
});

// Set new locale.
export async function setLocale(locale: string) {
  // Load locale if not available yet.
  if (!i18n.global.availableLocales.includes(locale)) {
    const messages = await loadLocale(locale);

    // fetch() error occurred.
    if (messages === undefined) {
      return;
    }

    // Add locale.
    i18n.global.setLocaleMessage(locale, messages);
  }

  // Set locale.
  i18n.global.locale.value = locale;

  // store
  localStorage.setItem(LOCALE_KEY, locale);
}

// Fetch locale.
async function loadLocale(locale: string): Promise<any> {
  return fetch(`../locales/${locale}.json`)
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error('Something went wrong!');
    })
    .catch((error) => {
      console.error(error);
    });
}

export const getLocale = () => {
  return i18n.global.locale.value;
};
