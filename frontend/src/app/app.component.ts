import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ApiService } from './services/api.service';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, BaseChartDirective],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'FitTrack Pro';
  
  // Dashboard Metrics
  totalWorkouts = 0;
  caloriesBurned = 0;
  currentBmi = 22.4;
  activeStreak = 0;
  isLoading = true;

  // File Upload & AI
  isUploading = false;
  ocrInsight = 'Based on your recent Heavy Leg Day, Gemini suggests focusing specifically on Protein recovery overnight.';

  // Modal State
  isLogModalOpen = false;
  workoutForm = new FormGroup({
    name: new FormControl('', Validators.required),
    weight: new FormControl(0, Validators.min(0)),
    sets: new FormControl(3, [Validators.min(1), Validators.required]),
    reps: new FormControl(10, [Validators.min(1), Validators.required])
  });

  // Analytics Global State Matrix
  public lineChartData: any = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [{
      data: [0, 0, 0, 0, 0, 0, 0],
      label: 'Calories Burned',
      fill: true,
      tension: 0.4,
      borderColor: '#10b981',
      backgroundColor: 'rgba(16, 185, 129, 0.15)',
      pointBackgroundColor: '#6366f1'
    }]
  };

  public lineChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: {
      y: { beginAtZero: true, grid: { color: 'rgba(255, 255, 255, 0.05)' }, ticks: { color: '#94a3b8' } },
      x: { grid: { display: false }, ticks: { color: '#94a3b8' } }
    }
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.sessionReady$.subscribe((ready: boolean) => {
      if (ready) {
        this.fetchDashboardData();
        
        // Ping Render backend every 14 minutes to prevent free-tier from sleeping
        setInterval(() => {
          this.apiService.getInsights().subscribe();
        }, 14 * 60 * 1000);
      }
    });

    // Bootstraps our mocked standard user session
    this.apiService.initializeMockSession("1234567890");
  }

  fetchDashboardData() {
    this.apiService.getInsights().subscribe({
      next: (data: any) => {
        this.totalWorkouts = data.totalWorkouts || 0;
        this.caloriesBurned = data.totalCaloriesBurned || 0;
        this.activeStreak = this.totalWorkouts; // Demo streak

        // Simulate a lively analytics update by mapping total calories dynamically along the week axis for visual MVP feedback
        const dist = Math.floor(Math.random() * 5) + 1;
        const chartArr = Array(7).fill(0);
        chartArr[chartArr.length - dist] = this.caloriesBurned; 
        
        // Re-assign object inherently updating ng2-charts ChangeDetection cleanly
        this.lineChartData = {
          ...this.lineChartData,
          datasets: [{ ...this.lineChartData.datasets[0], data: chartArr }]
        };

        this.isLoading = false;
      },
      error: (err: any) => console.error(err)
    });
  }

  openLogModal() {
    this.isLogModalOpen = true;
  }

  closeLogModal() {
    this.isLogModalOpen = false;
    this.workoutForm.reset({ weight: 0, sets: 3, reps: 10 });
  }

  submitWorkout() {
    if (this.workoutForm.valid) {
      const v = this.workoutForm.value;
      this.apiService.createWorkout(v.name!, v.weight!, v.reps!, v.sets!).subscribe({
        next: (res: any) => {
          console.log("Workout logged! Calories burned:", res.caloriesBurned);
          this.closeLogModal();
          this.fetchDashboardData(); // Refresh metrics natively!
        },
        error: (err: any) => console.error("Failed to log workout", err)
      });
    }
  }

  uploadDocument(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.apiService.analyzeDocument(file).subscribe({
        next: (res: any) => {
          this.ocrInsight = res.aiRecommendation;
          this.isUploading = false;
        },
        error: (err: any) => {
          console.error("AI Analysis failed:", err);
          this.isUploading = false;
        }
      });
    }
  }
}
