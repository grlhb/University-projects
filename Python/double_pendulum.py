import numpy as np 
import scipy.integrate
import matplotlib.pyplot as plt
import matplotlib.animation as animation

class DoublePendulum:

	def __init__(self, M1 = 1, L1 = 1, M2 = 1, L2 = 1, G = 9.81):
		self.M1 = M1 
		self.L1 = L1
		self.M2 = M2 
		self.L2 = L2
		self.G = G

	def __call__(self, t, y):
		"""Returns the RHS of the differential equations."""
		dtheta1 = y[1]
		dtheta2 = y[3]
		dt = y[2] - y[0]
		domega1 = ((self.M2 * self.L1 * y[1]**2 * np.sin(dt) * np.cos(dt)
					+ self.M2 * self.G * np.sin(y[2]) * np.cos(dt)
					+ self.M2 * self.L2 * y[3]**2 * np.sin(dt)
					- (self.M1 + self.M2) * self.G * np.sin(y[0]))
					/ ((self.M1 + self.M2) * self.L1 
					- self.M2 * self.L1 * np.cos(np.cos(dt))))
		domega2 = ((-self.M2 * self.L2 * y[3]**2 * np.sin(dt) * np.cos(dt)
					+ (self.M1 + self.M2) * self.G * np.sin(y[0]) * np.cos(dt)
					- (self.M1 + self.M2) * self.L1 * y[1]**2 * np.sin(dt)
					- (self.M1 + self.M2) * self.G * np.sin(y[2]))
					/ ((self.M1 + self.M2) * self.L2
					- self.M2 * self.L2 * np.cos(np.cos(dt))))
		return (dtheta1, domega1, dtheta2, domega2)

	def solve(self, y0, T, dt):
		"""Solves the differential equations."""
		self.sol = scipy.integrate.solve_ivp(self, (0,T), y0, t_eval = np.arange(0,T,dt), method = "Radau")
		self._solved = True
		

	
	def create_animation(self):
		"""Initiates an animation."""
		fig = plt.figure()
		plt.axis("equal")
		plt.axis("off")
		plt.axis((-5, 5, -5, 5))

		self.pendulums, = plt.plot([], [], "o-", lw=2)

		self.animation = animation.FuncAnimation(fig,
												 self._next_frame,
												 frames = range(len(self.x1)),
												 repeat = None,
												 interval = 1000/60,
												 blit = True)
		
			
	def _next_frame(self, i):
		"""Returns the coordinates for the animation."""
		self.pendulums.set_data((0, self.x1[i], self.x2[i]),
								(0, self.y1[i], self.y2[i]))
		return self.pendulums,
	
	def show_animation(self):
		plt.show()	

	def save_animation(self):
		Writer = animation.writers['ffmpeg']
		writer = Writer(fps = 60, bitrate = 1800)
		self.animation.save("pendulum_motion.mp4", fps = 60)
	
	@property 
	def t(self):
		if self._solved is True:
			return self.sol.t 

		else:
			raise AttributeError("Must solve the ODE first")

	@property 
	def theta1(self):
		if self._solved is True:
			return self.sol.y[0] 

		else:
			raise AttributeError("Must solve the ODE first")

	@property 
	def theta2(self):
		if self._solved is True:
			return self.sol.y[2] 

		else:
			raise AttributeError("Must solve the ODE first")

	@property
	def x1(self):
		return self.L1 * np.sin(self.theta1)

	@property 
	def y1(self):
		return -self.L1 * np.cos(self.theta1)		
			
	@property
	def x2(self):
		return self.x1 + self.L2 * np.sin(self.theta2)
		

	@property
	def y2(self):
		return self.y1 - self.L2 * np.cos(self.theta2)
	
	@property 
	def potential(self):
		return (self.M1 * self.G * (self.y1 + self.L1)
				+ self.M2 * self.G * (self.y2 + self.L1 + self.L2))

	@property
	def vx1(self):
		return np.gradient(self.x1, self.t)

	@property
	def vy1(self):
		return np.gradient(self.y1, self.t)

	@property
	def vx2(self):
		return np.gradient(self.x2, self.t)

	@property
	def vy2(self):
		return np.gradient(self.y2, self.t)

	@property
	def kinetic(self):
		return (0.5 * self.M1 * (self.vx1**2 + self.vy1**2)
				+ 0.5 * self.M2 * (self.vx2**2 + self.vy2**2))

if __name__ == "__main__":
	p = DoublePendulum(M1 = 2, L1 = 1.2,M2 = 0.5, L2 = 1)
	p.solve((np.pi/4, 0.2, np.pi/8, 0.3), 10, 1/60)
	plt.plot(p.t, p.kinetic, label = "Kinetic Energy")
	plt.plot(p.t, p.potential, label = "Potential Energy")
	plt.plot(p.t, p.kinetic + p.potential, label = "Total Energy")
	plt.xlabel("Time")
	plt.ylabel("Energy")
	plt.legend()
	plt.show()
	p.create_animation()
	p.save_animation()
