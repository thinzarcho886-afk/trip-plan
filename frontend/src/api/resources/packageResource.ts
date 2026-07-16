import { ApiResources } from '..';

// define api resource name
type PackageApiResource =
  | 'getPackages'
  | 'create'
  | 'save'
  | 'update'
  | 'getById';
const baseUrl = '/auth/package';

export const packageApiResource: ApiResources<PackageApiResource> = {
  getPackages: {method: 'get', url: baseUrl},
  create: { method: 'post', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
};
