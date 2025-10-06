import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;

  beforeEach(async () => {
    jest.restoreAllMocks();
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    authService = TestBed.inject(AuthService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register user on submit', () => {
      const spiedRegister = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
      const router = TestBed.inject(Router);
      const spiedNavigate = jest.spyOn(router, 'navigate');
      
      component.form.setValue({
        email: "test@test.com",
        firstName: "Test",
        lastName: "TEST",
        password: "test!1234"
      });
      const registerRequest = component.form.value as RegisterRequest;
      component.submit();
      
      expect(spiedRegister).toHaveBeenCalledWith(registerRequest);
      expect(spiedNavigate).toHaveBeenCalledWith(['/login']);
    });

    it('should display error message', () => {
      const spiedRegister = jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error()));
      
      component.submit();
      
      expect(spiedRegister).toHaveBeenCalled();
      const errorSpanElement = fixture.nativeElement.querySelector("span.error");
      expect(errorSpanElement).toBeDefined();
    });
});
