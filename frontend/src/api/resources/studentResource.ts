/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type StudentApiResource =
  | 'createStudent'
  | 'updateStudent'
  | 'getAllStudents'
  | 'getStudentById'
  | 'getStudentByName'
  | 'getStudentByPhone'
  | 'getStudentByAddress';
export const studentApiResource: ApiResources<StudentApiResource> = {
  createStudent: { method: 'post', url: '/auth/student' },
  updateStudent: { method: 'put', url: '/auth/student' },
  getAllStudents:{ method: 'get', url: '/auth/student/all' },
  getStudentById: { method: 'get', url: '/auth/student/:id' },
  getStudentByName: { method: 'post', url: '/auth/student/search-by-name' },
  getStudentByPhone:{ method: 'post', url: '/auth/student/search-by-phone' },
  getStudentByAddress:{ method: 'post', url: '/auth/student/search-by-address' },
}
