/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type UserApiResource =
  | 'login'
  | 'register'
  | 'getUsers'
  | 'getById'
  | 'save'
  | 'update'
  | 'changePassword'
  | 'changePasswordForUser';

// const baseURL = '/api';

export const userApiResource: ApiResources<UserApiResource> = {
  login: { method: 'post', url: '/login' },
  register: {method: 'post', url: '/register'},
  getUsers:{ method: 'get', url: '/auth/user'},
  getById: { method: 'get', url: '/auth/user/:id' },
  save: { method: 'post', url: '/auth/user' },
  update: { method: 'put', url: '/auth/user' },
  changePassword: {
    method: 'put',
    url: '/auth/change-password-by-current-user',
  },
  changePasswordForUser: { method: 'put', url: '/auth/change-password-by-id' },
};
