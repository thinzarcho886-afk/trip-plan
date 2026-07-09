import { defineStore } from 'pinia';
import { Role } from '../constants/Role';
import { AuthService } from '../services/AuthService';

const authService: AuthService = new AuthService();

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: authService.getAuthUser(),
    isAuth: authService.isAuth(),
  }),
  getters: {
    userRole: (state): Role => state.user?.role as Role,
  },
  actions: {
    store(user: any, token: string) {
      authService.store(user, token);
      this.user = user;
      this.isAuth = true;
    },
    clear() {
      authService.purge();
      this.user = null;
      this.isAuth = false;
    },
  },
});
