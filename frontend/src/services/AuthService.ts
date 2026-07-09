const _PREFIX_ = import.meta.env.VITE_APP_LOCALSTORAGE_PREFIX;

const USER_KEY = _PREFIX_ + 'user';
const TOKEN_KEY = _PREFIX_ + 'token';

export class AuthService {
  store(user: any, token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  getBearerAuth(): string | null {
    const accessToken: string | null = localStorage.getItem(TOKEN_KEY);
    if (!accessToken) return null;

    return 'Bearer ' + accessToken;
  }

  isAuth(): boolean {
    const token: string | null = localStorage.getItem(TOKEN_KEY);
    const user: string | null = localStorage.getItem(USER_KEY);

    return !!token && !!user;
  }

  getAuthUser(): any | null {
    if (!this.isAuth()) return null;

    const user: string | null = localStorage.getItem(USER_KEY);
    if (!user) return null;

    return JSON.parse(user);
  }

  purge(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }
}
