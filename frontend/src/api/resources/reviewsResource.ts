import { ApiResources } from '..';

// define api resource name
type ReviewsApiResource =
  | 'getReviews'
  | 'register'
  | 'getByStudentName';

const baseUrl = '/auth/reviews';

export const reviewsApiResource: ApiResources<ReviewsApiResource> = {
  getReviews: { method: 'get', url: baseUrl },
  register: { method: 'post', url: baseUrl },
  getByStudentName: { method: 'get', url: baseUrl + '/by-student-name/:studentName' },
  
};