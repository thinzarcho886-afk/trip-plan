import { ApiResources } from '..';

// define api resource name
type DurationApiResource =
  | 'getDurations'
  | 'register'
  | 'save'
  | 'update'
  | 'getById'
  | 'getByStatus';

const baseUrl = '/auth/duration';

export const durationApiResource: ApiResources<DurationApiResource> = {
  getDurations: {method: 'get', url: baseUrl},
  register: { method: 'post', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
