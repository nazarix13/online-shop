import { Component, OnInit } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {NgIf} from "@angular/common";
import {AuthService} from "../../services/auth-service/auth.service";
import {NotificationService} from "../../services/notification/notification.service";
import {LocalStorageService} from "../../services/storage-service/local-storage.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    NgIf,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  validateForm!: FormGroup;

  confirmationValidator = (control: FormControl): { [s: string]: boolean } | null => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validateForm.controls['password'].value) {
      return { confirm: true, error: true };
    }
    return null;
  };

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private notificationService: NotificationService,
              private router: Router,) { }

  ngOnInit() {
    this.validateForm = this.fb.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      confirmPassword: [
        null,
        [Validators.required, this.confirmationValidator]
      ]
    });

    this.validateForm.controls['password'].valueChanges.subscribe(() => {
      this.validateForm.controls['confirmPassword'].updateValueAndValidity();
    });
  }

  register() {
    this.authService.register(this.validateForm.value).subscribe((res) => {
      this.notificationService.showSuccess('You successfully registered!');
      if (LocalStorageService.isAdminLoggedIn()) {
        this.router.navigateByUrl('/admin/dashboard');
      } else if (LocalStorageService.isUserLoggedIn()) {
        this.router.navigateByUrl('/user/dashboard');
      }
    }, error => {
      console.log(error.message);
      this.notificationService.showError('Registration failed!');
    });
  }
}