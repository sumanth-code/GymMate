import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ApiService } from './services/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
  title = 'FitTrack Pro';
  
  constructor(private apiService: ApiService) {}

  ngOnInit() {
    // Bootstraps our mocked standard user session globally for the app
    this.apiService.initializeMockSession("1234567890");
  }
}
