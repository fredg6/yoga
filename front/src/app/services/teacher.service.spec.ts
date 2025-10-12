import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let spiedHttpClientGet: jest.SpyInstance;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
    spiedHttpClientGet = jest.spyOn(TestBed.inject(HttpClient), 'get');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
    
  it('should call httpClient.get() and return an observable that emits the requested teacher details on detail', () => {
    const id = '1';
    const mockedTeacher = {
      id: 1,
      lastName: "Teacher",
      firstName: "Margaret",
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.detail(id).subscribe(value => {
      expect(spiedHttpClientGet).toHaveBeenCalledWith(`${service['pathService']}/${id}`);
      expect(value).toEqual(mockedTeacher);
    });
  });
});
