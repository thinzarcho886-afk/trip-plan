/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';
// define api resource name
type HostelApiResource =
  | 'login'
  | 'getList'
  | 'getById'
  | 'save'
  | 'update'
  | 'changePassword'
  | 'changePasswordForUser';

  const baseURL = '/auth/hostel';

export const hostelApiResource: ApiResources<HostelApiResource> = {
  login: { method: 'post', url: '/auth/login' },
  getList: { method: 'get', url: '/auth/hostel' },
  getById: { method: 'get', url: '/auth/hostel/:id' },
  save: { method: 'post', url: '/auth/hostel' },
  update: { method: 'put', url: '/auth/hostel' },
  changePassword: {
    method: 'put',
    url: '/auth/change-password-by-current-user',
  },
  changePasswordForUser: { method: 'put', url: '/auth/change-password-by-id' },
};
